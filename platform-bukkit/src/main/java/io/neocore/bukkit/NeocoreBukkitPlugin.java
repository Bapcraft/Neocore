package io.neocore.bukkit;

import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import io.neocore.Neocore;
import io.neocore.NeocoreImpl;
import io.neocore.bukkit.events.ChatEventForwarder;
import io.neocore.bukkit.events.EventForwarder;
import io.neocore.bukkit.providers.BukkitBroadcastService;
import io.neocore.bukkit.providers.BukkitChatService;
import io.neocore.bukkit.providers.BukkitLoginService;
import io.neocore.host.HostPlugin;
import io.neocore.host.HostService;

public class NeocoreBukkitPlugin extends JavaPlugin implements HostPlugin {
	
	public static NeocoreBukkitPlugin inst;
	
	private BukkitBroadcastService broadcastService;
	private BukkitChatService chatService;
	private BukkitLoginService loginService;
	
	private ArrayList<EventForwarder> forwarders;
	private ChatEventForwarder chatForwarder;
	
	@Override
	public void onEnable() {
		
		inst = this;
		
		// Initialize Neocore.
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
		
		// Event registration
		for (EventForwarder fwdr : this.forwarders) {
			Bukkit.getPluginManager().registerEvents(fwdr, this);
		}
		
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
		return "${project.version}";
	}
	
}
