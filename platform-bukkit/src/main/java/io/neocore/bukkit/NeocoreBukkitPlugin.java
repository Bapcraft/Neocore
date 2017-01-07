package io.neocore.bukkit;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.justisr.BungeeCom;
import com.typesafe.config.ConfigFactory;

import io.neocore.api.NeocoreAPI;
import io.neocore.api.NeocoreConfig;
import io.neocore.api.NeocoreInstaller;
import io.neocore.api.cmd.AbstractCommand;
import io.neocore.api.host.Context;
import io.neocore.api.host.HostService;
import io.neocore.api.host.Scheduler;
import io.neocore.api.infrastructure.InfrastructureService;
import io.neocore.api.infrastructure.NetworkMapService;
import io.neocore.api.player.PlayerManager;
import io.neocore.api.task.DumbTaskDelegator;
import io.neocore.api.task.Task;
import io.neocore.bukkit.cmd.CommandInjector;
import io.neocore.bukkit.cmd.CommandInjector_19r2;
import io.neocore.bukkit.cmd.CommandNativeDumpPerms;
import io.neocore.bukkit.events.ChatEventForwarder;
import io.neocore.bukkit.events.EventForwarder;
import io.neocore.bukkit.events.PlayerConnectionForwarder;
import io.neocore.bukkit.events.PluginModuleAutloader;
import io.neocore.bukkit.events.wrappers.BukkitServerInitializedEvent;
import io.neocore.bukkit.services.BukkitBroadcastService;
import io.neocore.bukkit.services.BukkitChatService;
import io.neocore.bukkit.services.BukkitLoginService;
import io.neocore.bukkit.services.network.StandaloneNetworkMapService;
import io.neocore.bukkit.services.permissions.BukkitPermsService;
import io.neocore.bukkit.services.permissions.PbInjectionListener;
import io.neocore.common.FullHostPlugin;
import io.neocore.common.JavaNativeScheduler;
import io.neocore.common.NeocoreImpl;

public class NeocoreBukkitPlugin extends JavaPlugin implements FullHostPlugin {
	
	public static NeocoreBukkitPlugin inst;
	private NeocoreImpl neocore;
	
	// Metadata
	private BukkitNeocoreConfig config;
	
	// Host services
	private BukkitLoginService loginService;
	private BukkitBroadcastService broadcastService;
	private BukkitPermsService permsService;
	private BukkitChatService chatService;
	
	// Potential infrastructure services.
	private NetworkMapService netMapService;
	
	// Forwarders
	private List<EventForwarder> forwarders = new ArrayList<>();
	private PlayerConnectionForwarder connectionForwarder;
	private ChatEventForwarder chatForwarder;
	
	// Utils
	private BungeeCom bungee;
	private CommandInjector cmdInjector;
	private Scheduler scheduler;
	
	@Override
	public void onEnable() {
		
		inst = this;
		
		// Initialize config
		BukkitNeocoreConfig.verifyConfig(this.getConfigFile(), this);
		this.config = new BukkitNeocoreConfig(ConfigFactory.parseFile(this.getConfigFile()));

		// Set up command injector.
		this.cmdInjector = new CommandInjector_19r2(); // FIXME Make this figure out the details automatically.
		this.scheduler = new JavaNativeScheduler(this.getLogger());
		
		// Initialize and install Neocore
		NeocoreInstaller.applyLogger(this.getLogger());
		NeocoreImpl neo = new NeocoreImpl(this);
		NeocoreInstaller.install(neo);
		this.neocore = neo; // Alias because we use it a lot here.
		
		// Support classes
		this.chatForwarder = new ChatEventForwarder();
		this.forwarders.add(this.chatForwarder);
		this.connectionForwarder = new PlayerConnectionForwarder(neo);
		this.forwarders.add(this.connectionForwarder);
		
		// Services
		this.loginService = new BukkitLoginService(this.connectionForwarder);
		this.broadcastService = new BukkitBroadcastService();
		this.permsService = new BukkitPermsService(this);
		this.chatService = new BukkitChatService(this.chatForwarder);
		
		// Register services properly with Neocore.
		neo.registerServiceProvider(HostService.LOGIN, this.loginService, this);
		neo.registerServiceProvider(HostService.BROADCAST, this.broadcastService, this);
		neo.registerServiceProvider(HostService.PERMISSIONS, this.permsService, this);
		neo.registerServiceProvider(HostService.CHAT, this.chatService, this);
		// TODO Gameplay (needs API interface definitions first)
		
		// Configure the services that we can provide from Bukkit/Spigot.
		PlayerManager pm = neo.getPlayerManager();
		pm.addService(HostService.LOGIN);
		pm.addService(HostService.PERMISSIONS);
		pm.addService(HostService.CHAT);
		
		// Do the network stuff all at once if we need to do it at all.
		if (this.config.isNetworked()) {
			NeocoreAPI.getLogger().warning("It is strongly suggested that you run Neocore in standalone mode if you do not have a sync daemon.");
		} else {
			
			this.netMapService = new StandaloneNetworkMapService(this.neocore);
			neo.registerServiceProvider(InfrastructureService.NETWORKMAP, this.netMapService, this);
			
		}
		
		// Event forwarder registration
		PluginManager pl = Bukkit.getPluginManager();
		for (EventForwarder fwdr : this.forwarders) {
			pl.registerEvents(fwdr, this);
		}
		
		// FIXME Clean up how the server start tasks and stuff are set up.
		pl.registerEvents(new PluginModuleAutloader(pl), this);
		pl.registerEvents(new PbInjectionListener(this.permsService), this);
		
		// Set up a broadcast for server initialization.
		neo.getTaskQueue().enqueue(new Task(new DumbTaskDelegator("Neocore-Init")) {
			
			@Override
			public void run() {
				neo.getEventManager().broadcast(new BukkitServerInitializedEvent());
			}
			
		});

		// Queue up a thing to initialize Neocore.
		this.getServer().getScheduler().runTask(this, () -> {
			
			/*
			 * If a player connects before this actually gets invoked then the
			 * prelogin event handler will initialze neocore from that thread,
			 * then once this is allowed to enter it will exit it pretty
			 * quickly because it's already inited.
			 */
			neo.init();
			
		});
		
		// Native commands.
		this.registerCommand(new CommandNativeDumpPerms());
		
	}
	
	@Override
	public void onDisable() {
		
		inst = null;
		
		// Services
		this.broadcastService = null;
		this.chatService = null;
		this.loginService = null;
		
		// Support classes
		this.forwarders = null;
		this.chatForwarder = null;
		
		// Everything else gets done automatically.
		this.neocore.shutdown();
		
	}
	
	@Override
	public String getVersion() {
		return "0.0-DEVELOPMENT";
	}
	
	@Override
	public File getMicromoduleDirectory() {
		return new File("micromodules");
	}
	
	@Override
	public NeocoreConfig getNeocoreConfig() {
		return this.config;
	}
	
	public BungeeCom getProxyCommunication() {
		return this.bungee;
	}
	
	public File getConfigFile() {
		return new File(this.getDataFolder(), BukkitNeocoreConfig.CONFIG_FILE_NAME);
	}
	
	@Override
	public File getDatabaseConfigFile() {
		return new File(this.getDataFolder(), "databases.conf");
	}
	
	@Override
	public Context getPrimaryContext() {
		return this.config.getPrimaryContext();
	}

	@Override
	public List<Context> getContexts() {
		return this.config.getContexts();
	}

	@Override
	public void registerCommand(AbstractCommand cmd) {
		this.cmdInjector.inject(cmd);
	}

	@Override
	public boolean isFrontServer() {
		return !this.getNeocoreConfig().isNetworked();
	}

	@Override
	public Scheduler getScheduler() {
		return this.scheduler;
	}
	
}
