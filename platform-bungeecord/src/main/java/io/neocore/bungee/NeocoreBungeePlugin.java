package io.neocore.bungee;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import com.typesafe.config.ConfigFactory;

import io.neocore.api.Neocore;
import io.neocore.api.NeocoreAPI;
import io.neocore.api.NeocoreConfig;
import io.neocore.api.NeocoreInstaller;
import io.neocore.api.host.Context;
import io.neocore.api.host.HostContext;
import io.neocore.api.host.HostService;
import io.neocore.api.task.DumbTaskDelegator;
import io.neocore.api.task.Task;
import io.neocore.bungee.broadcast.BungeeBroadcastService;
import io.neocore.bungee.events.EventForwarder;
import io.neocore.bungee.events.ProxyForwarder;
import io.neocore.bungee.network.BungeeProxyService;
import io.neocore.common.FullHostPlugin;
import io.neocore.common.HostPlayerInjector;
import io.neocore.common.NeocoreImpl;
import net.md_5.bungee.api.plugin.Plugin;

public class NeocoreBungeePlugin extends Plugin implements FullHostPlugin {
	
	public static NeocoreBungeePlugin inst;
	
	private BungeeNeocoreConfig config;
	
	private BungeeProxyService proxyService;
	private BungeeBroadcastService broadcastService;
	
	private List<EventForwarder> forwarders = new ArrayList<>();
	private ProxyForwarder proxyForwarder;
	
	@Override
	public void onEnable() {
		
		inst = this;
		
		NeocoreInstaller.applyLogger(null);
		Neocore neo = new NeocoreImpl(this);
		
		BungeeNeocoreConfig.verifyConfig(this.getConfigFile(), this);
		this.config = new BungeeNeocoreConfig(ConfigFactory.parseFile(this.getConfigFile()));
		
		// Support classes
		this.proxyForwarder = new ProxyForwarder();
		this.forwarders.add(this.proxyForwarder);
		
		// Services
		this.proxyService = new BungeeProxyService(this.getProxy());
		this.broadcastService = new BungeeBroadcastService();
		
		// Actually register the services.
		// TODO Login
		// TODO Session
		neo.registerServiceProvider(HostService.BROADCAST, this.broadcastService, this);
		neo.registerServiceProvider(HostService.PROXY, this.proxyService, this);
		// TODO Permissions
		
		// Set up a broadcast for server initialization.
		neo.getTaskQueue().enqueue(new Task(new DumbTaskDelegator("Neocore-Init")) {
			
			@Override
			public void run() {
				
				NeocoreAPI.announceCompletion();
				
			}
			
		});
		
	}
	
	@Override
	public void onDisable() {
		
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
	public HostPlayerInjector getPlayerInjector() {
		
		// TODO Make the player injector work.
		return null;
		
	}
	
}
