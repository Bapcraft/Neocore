package io.neocore.bukkit;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import io.neocore.NeocoreImpl;
import io.neocore.api.Neocore;
import io.neocore.api.NeocoreAPI;
import io.neocore.api.NeocoreInstaller;
import io.neocore.api.RegisteredService;
import io.neocore.api.ServiceType;
import io.neocore.api.database.DatabaseController;
import io.neocore.api.database.DatabaseManager;
import io.neocore.api.host.HostPlugin;
import io.neocore.api.host.HostService;
import io.neocore.api.module.Module;
import io.neocore.bukkit.events.ChatEventForwarder;
import io.neocore.bukkit.events.EventForwarder;
import io.neocore.bukkit.providers.BukkitBroadcastService;
import io.neocore.bukkit.providers.BukkitChatService;
import io.neocore.bukkit.providers.BukkitLoginService;

public class NeocoreBukkitPlugin extends JavaPlugin implements HostPlugin {
	
	public static NeocoreBukkitPlugin inst;
	
	private BukkitBroadcastService broadcastService;
	private BukkitChatService chatService;
	private BukkitLoginService loginService;
	
	private List<EventForwarder> forwarders = new ArrayList<>();
	private ChatEventForwarder chatForwarder;
	
	@Override
	public void onEnable() {
		
		inst = this;
		
		// Initialize and install Neocore.
		NeocoreInstaller.applyLogger(Bukkit.getLogger());
		Neocore neo = new NeocoreImpl(this);
		
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
		
		this.announceCompletion();
		
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
	
	private void announceCompletion() {
		
		Logger log = Bukkit.getLogger();
		log.info("=== Neocore Initialization Complete ===");
		
		Neocore neo = NeocoreAPI.getAgent();
		
		// Display host info
		log.info("Host Class: " + neo.getHost().getClass().getName());
		log.info("Host Version: " + neo.getHost().getVersion());
		
		// Display module info
		log.info("Registered Modules:");
		Set<Module> mods = neo.getModuleManager().getModules();
		for (Module m : mods) {
			log.info(String.format(" - %s v%s", m.getName(), m.getVersion()));
		}
		
		// Display database engines
		log.info("Database Controllers:");
		DatabaseManager dbm = neo.getDatabaseManager();
		for (Class<? extends DatabaseController> dbc : dbm.getControllers()) {
			log.info(" - " + dbc.getSimpleName());
		}
		
		// Display service info
		log.info("Registerd Service Providers:");
		List<RegisteredService> servs = neo.getServiceManager().getServices();
		for (RegisteredService rs : servs) {
			ServiceType st = rs.getType();
			log.info(String.format(" - %s: %s %s (%s)", st.getName(), rs.getModule().getName(), rs.getServiceProvider().getClass().getSimpleName(), st.getClass().getSimpleName()));
		}
		
		// List off all the other services that need to be dealt with eventually
		log.info("Unprovisioned Services:");
		List<ServiceType> types = neo.getServiceManager().getUnprovidedServices();
		for (ServiceType st : types) {
			log.info(String.format(" - %s (%s)", st.getName(), st.getClass().getSimpleName()));
		}
		
		log.info("=======================================");
		
	}
	
}
