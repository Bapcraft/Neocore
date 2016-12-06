package io.neocore.bungee;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.typesafe.config.ConfigFactory;

import io.neocore.api.NeocoreConfig;
import io.neocore.api.NeocoreInstaller;
import io.neocore.api.cmd.AbstractCommand;
import io.neocore.api.host.Context;
import io.neocore.api.host.HostContext;
import io.neocore.api.host.HostService;
import io.neocore.api.host.Scheduler;
import io.neocore.api.infrastructure.InfrastructureService;
import io.neocore.api.task.DumbTaskDelegator;
import io.neocore.api.task.Task;
import io.neocore.bungee.cmd.CommandWrapper;
import io.neocore.bungee.events.EventForwarder;
import io.neocore.bungee.events.PlayerConnectionForwarder;
import io.neocore.bungee.network.BungeeNetworkMapService;
import io.neocore.bungee.network.BungeeProxyService;
import io.neocore.bungee.services.BungeeBroadcastService;
import io.neocore.bungee.services.BungeeLoginService;
import io.neocore.bungee.services.BungeePermissionService;
import io.neocore.common.FullHostPlugin;
import io.neocore.common.NeocoreImpl;
import net.md_5.bungee.api.plugin.Plugin;

public class NeocoreBungeePlugin extends Plugin implements FullHostPlugin {
	
	public static NeocoreBungeePlugin inst;
	private NeocoreImpl neocore;
	
	private BungeeNeocoreConfig config;
	
	private BungeeLoginService loginService;
	private BungeeProxyService proxyService;
	private BungeePermissionService permsService;
	private BungeeBroadcastService broadcastService;
	private BungeeNetworkMapService netMapService;
	
	private List<EventForwarder> forwarders = new ArrayList<>();
	private PlayerConnectionForwarder loginForwarder;
	
	private BungeeScheduler scheduler;
	
	@Override
	public void onEnable() {
		
		inst = this;
		
		BungeeNeocoreConfig.verifyConfig(this.getConfigFile(), this);
		this.config = new BungeeNeocoreConfig(ConfigFactory.parseFile(this.getConfigFile()));
		
		NeocoreInstaller.applyLogger(this.getProxy().getLogger());
		NeocoreImpl neo = new NeocoreImpl(this);
		NeocoreInstaller.install(neo);
		this.neocore = neo; // Alias because we use it a lot here.
		
		this.scheduler = new BungeeScheduler(this, this.getProxy().getScheduler());
		
		// Support classes
		this.loginForwarder = new PlayerConnectionForwarder(neo);
		this.forwarders.add(this.loginForwarder);
		
		// Services
		this.loginService = new BungeeLoginService(this.loginForwarder);
		this.proxyService = new BungeeProxyService(this.loginForwarder);
		this.permsService = new BungeePermissionService();
		this.broadcastService = new BungeeBroadcastService(this.getProxy());
		this.netMapService = new BungeeNetworkMapService(this.getNeocoreConfig().getServerName(), this.getProxy());
		
		// Actually register the services.
		neo.registerServiceProvider(HostService.LOGIN, this.loginService, this);
		neo.registerServiceProvider(HostService.BROADCAST, this.broadcastService, this);
		neo.registerServiceProvider(HostService.PERMISSIONS, this.permsService, this);
		neo.registerServiceProvider(InfrastructureService.PROXY, this.proxyService, this);
		neo.registerServiceProvider(InfrastructureService.NETWORKMAP, this.netMapService, this);
		
		// Set up a broadcast for server initialization.
		neo.getTaskQueue().enqueue(new Task(new DumbTaskDelegator("Neocore-Init")) {
			
			@Override
			public void run() {
				neo.getEventManager().broadcast(new BungeeServerInitializedEvent());
			}
			
		});
		
		// Queue up a thing to initialize Neocore.
		this.getProxy().getScheduler().runAsync(this, () -> {
			
			/*
			 * If a player connects before this actually gets invoked then the
			 * prelogin event handler will initialze neocore from that thread,
			 * then once this is allowed to enter it will exit it pretty
			 * quickly because it's already inited.
			 */
			neo.init();
			
		});
		
	}
	
	@Override
	public void onDisable() {
		
		// Do all the other things automatically.
		this.neocore.shutdown();
		
	}
	
	private File getConfigFile() {
		return new File(this.getDataFolder(), BungeeNeocoreConfig.CONFIG_FILE_NAME);
	}
	
	@Override
	public File getMicromoduleDirectory() {
		return new File(this.getProxy().getPluginsFolder().getParentFile(), "micromodules");
	}

	@Override
	public NeocoreConfig getNeocoreConfig() {
		return this.config;
	}

	@Override
	public File getDatabaseConfigFile() {
		return new File(this.getDataFolder(), "databases.conf");
	}
	
	@Override
	public HostContext getPrimaryContext() {
		return this.config.getPrimaryContext();
	}
	
	@Override
	public List<Context> getContexts() {
		return this.config.getContexts();
	}
	
	@Override
	public String getName() {
		return "Neocore";
	}
	
	@Override
	public String getVersion() {
		return "0.0-DEVELOPMENT";
	}
	
	@Override
	public void registerCommand(AbstractCommand cmd) {
		this.getProxy().getPluginManager().registerCommand(this, new CommandWrapper(cmd));
	}
	
	@Override
	public boolean isFrontServer() {
		return true;
	}
	
	@Override
	public Scheduler getScheduler() {
		return this.scheduler;
	}
	
}
