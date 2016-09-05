package io.neocore.bukkit;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.justisr.BungeeCom;
import com.typesafe.config.ConfigFactory;

import io.neocore.api.Neocore;
import io.neocore.api.NeocoreAPI;
import io.neocore.api.NeocoreConfig;
import io.neocore.api.NeocoreInstaller;
import io.neocore.api.host.Context;
import io.neocore.api.host.HostContext;
import io.neocore.api.host.HostService;
import io.neocore.api.host.ServerInitializedEvent;
import io.neocore.api.task.DumbTaskDelegator;
import io.neocore.api.task.Task;
import io.neocore.bukkit.events.ChatEventForwarder;
import io.neocore.bukkit.events.EventForwarder;
import io.neocore.bukkit.events.NeocoreRevalidator;
import io.neocore.bukkit.events.PlayerConnectionForwarder;
import io.neocore.bukkit.events.wrappers.BukkitServerInitializedEvent;
import io.neocore.bukkit.services.BukkitBroadcastService;
import io.neocore.bukkit.services.BukkitChatService;
import io.neocore.bukkit.services.BukkitLoginService;
import io.neocore.bukkit.services.network.BukkitEndpointService;
import io.neocore.bukkit.services.network.SelfEndpoint;
import io.neocore.common.FullHostPlugin;
import io.neocore.common.HostPlayerInjector;
import io.neocore.common.NeocoreImpl;
import io.neocore.common.tasks.Worker;

public class NeocoreBukkitPlugin extends JavaPlugin implements FullHostPlugin {
	
	public static NeocoreBukkitPlugin inst;
	
	private BukkitNeocoreConfig config;
	
	private BukkitBroadcastService broadcastService;
	private BukkitChatService chatService;
	private BukkitLoginService loginService;
	
	private SelfEndpoint selfEndpoint;
	private BukkitEndpointService endpointService;
	
	private List<EventForwarder> forwarders = new ArrayList<>();
	private ChatEventForwarder chatForwarder;
	private PlayerConnectionForwarder connectionForwarder;
	
	private BungeeCom bungee;
	
	@SuppressWarnings("deprecation")
	@Override
	public void onEnable() {
		
		inst = this;
		
		// Initialize config
		BukkitNeocoreConfig.verifyConfig(this.getConfigFile(), this);
		this.config = new BukkitNeocoreConfig(ConfigFactory.parseFile(this.getConfigFile()));
		
		// Initialize and install Neocore
		NeocoreInstaller.applyLogger(Bukkit.getLogger());
		Neocore neo = new NeocoreImpl(this);
		
		// Support classes
		this.chatForwarder = new ChatEventForwarder();
		this.forwarders.add(this.chatForwarder);
		this.connectionForwarder = new PlayerConnectionForwarder();
		this.forwarders.add(this.connectionForwarder);
		
		// Services
		this.broadcastService = new BukkitBroadcastService();
		this.chatService = new BukkitChatService(this.chatForwarder);
		this.loginService = new BukkitLoginService(this.connectionForwarder);
		
		// FIXME Make these register *before* we load the micromodules, somehow.
		// Register services properly with Neocore.
		neo.registerServiceProvider(HostService.LOGIN, this.loginService, this);
		neo.registerServiceProvider(HostService.BROADCAST, this.broadcastService, this);
		// TODO Permissions
		neo.registerServiceProvider(HostService.CHAT, this.chatService, this);
		// TODO Gameplay (needs API interface definitions first)
		
		// Do the network stuff all at once if we need to do it at all.
		if (this.config.isNetworked()) {
			
			this.selfEndpoint = new SelfEndpoint(this.getNeocoreConfig());
			this.endpointService = new BukkitEndpointService(this.selfEndpoint);
			neo.registerServiceProvider(HostService.ENDPOINT, this.endpointService, this);
			
		}
		
		// Event forwarder registration
		PluginManager pl = Bukkit.getPluginManager();
		for (EventForwarder fwdr : this.forwarders) {
			pl.registerEvents(fwdr, this);
		}
		
		// FIXME Clean up how the server start tasks and stuff are set up.
		pl.registerEvents(new NeocoreRevalidator(pl), this);
		
		// Set up a broadcast for server initialization.
		neo.getTaskQueue().enqueue(new Task(new DumbTaskDelegator("Neocore-Init")) {
			
			@Override
			public void run() {
				
				neo.getEventManager().broadcast(ServerInitializedEvent.class, new BukkitServerInitializedEvent());
				NeocoreAPI.announceCompletion();
				
			}
			
		});
		
		// This is deprecated because "the name is misleading".
		Bukkit.getScheduler().scheduleAsyncDelayedTask(this, new Worker(neo.getTaskQueue(), NeocoreAPI.getLogger()));
		
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
	public HostPlayerInjector getPlayerInjector() {
		return new BukkitPlayerInjector();
	}

	@Override
	public HostContext getPrimaryContext() {
		return this.config.getPrimaryContext();
	}

	@Override
	public List<Context> getContexts() {
		return this.config.getContexts();
	}
	
}
