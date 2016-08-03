package io.neocore.bukkit.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;

import io.neocore.api.NeocoreAPI;
import io.neocore.api.host.login.LoginAcceptor;
import io.neocore.api.player.NeoPlayer;
import io.neocore.bukkit.events.wrappers.BukkitInitialLoginEvent;
import io.neocore.bukkit.events.wrappers.BukkitPostJoinEvent;
import io.neocore.bukkit.events.wrappers.BukkitQuitEvent;
import io.neocore.bukkit.events.wrappers.BukkitServerPingEvent;

public class PlayerConnectionForwarder extends EventForwarder {
	
	public LoginAcceptor acceptor;
	
	@EventHandler
	public void onPing(ServerListPingEvent event) {
		
		BukkitServerPingEvent neoEvent = new BukkitServerPingEvent(event);
		
		// We don't need to do anything special for this so a standard broadcast event.
		NeocoreAPI.getAgent().getEventManager().broadcast(neoEvent); 
		
	}
	
	@EventHandler
	public void onPlayerLogin(PlayerLoginEvent event) {
		
		BukkitInitialLoginEvent neoEvent = new BukkitInitialLoginEvent(event);
		this.acceptor.onInitialLoginEvent(neoEvent);
		
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		
		NeoPlayer np = NeocoreAPI.getAgent().getPlayer(event.getPlayer().getUniqueId());
		BukkitPostJoinEvent neoEvent = new BukkitPostJoinEvent(event, np);
		this.acceptor.onPostLoginEvent(neoEvent);
		
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		
		NeoPlayer np = NeocoreAPI.getAgent().getPlayer(event.getPlayer().getUniqueId());
		BukkitQuitEvent neoEvent = new BukkitQuitEvent(event, np);
		this.acceptor.onDisconnectEvent(neoEvent);
		
	}
	
}
