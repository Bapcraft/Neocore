package io.neocore.bukkit;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.justisr.BungeeCom;
import com.typesafe.config.ConfigFactory;

import io.neocore.NeocoreImpl;
import io.neocore.api.Neocore;
import io.neocore.api.NeocoreAPI;
import io.neocore.api.NeocoreConfig;
import io.neocore.api.NeocoreInstaller;
import io.neocore.api.host.HostPlugin;
import io.neocore.api.host.HostService;
import io.neocore.bukkit.events.ChatEventForwarder;
import io.neocore.bukkit.events.EventForwarder;
import io.neocore.bukkit.providers.BukkitBroadcastService;
import io.neocore.bukkit.providers.BukkitChatService;
import io.neocore.bukkit.providers.BukkitLoginService;

public class NeocoreBukkitPlugin extends JavaPlugin implements HostPlugin {
	
	public static NeocoreBukkitPlugin inst;
	
	private BukkitNeocoreConfig config;
	
	private BukkitBroadcastService broadcastService;
	private BukkitChatService chatService;
	private BukkitLoginService loginService;
	
	private List<EventForwarder> forwarders = new ArrayList<>();
	private ChatEventForwarder chatForwarder;
	
	private BungeeCom bungee;
	
	@Override
	public void onEnable() {
		
		inst = this;
		
		// Initialize and install Neocore.
		NeocoreInstaller.applyLogger(Bukkit.getLogger());
		Neocore neo = new NeocoreImpl(this);
		
		this.config = new BukkitNeocoreConfig(ConfigFactory.parseFile(this.getConfigFile()));
		
		// Support classes
		this.chatForwarder = new ChatEventForwarder();
		this.forwarders.add(this.chatForwarder);
		
		// Services
		this.broadcastService = new BukkitBroadcastService();
		this.chatService = new BukkitChatService(this.chatForwarder);
		this.loginService = new BukkitLoginService();
		
		// Register services properly with Neocore.
		neo.registerServiceProvider(HostService.BROADCAST, this.broadcastService, this);
		neo.registerServiceProvider(HostService.CHAT, this.chatService, this);
		neo.registerServiceProvider(HostService.LOGIN, this.loginService, this);
		
		// Event forwarder registration
		for (EventForwarder fwdr : this.forwarders) {
			Bukkit.getPluginManager().registerEvents(fwdr, this);
		}
		
		NeocoreAPI.announceCompletion();
		
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
		return new File(this.getDataFolder(), "neocore.conf");
	}

}
