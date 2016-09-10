package io.neocore.bukkit.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerQuitEvent;

import io.neocore.api.NeocoreAPI;
import io.neocore.api.host.login.LoginAcceptor;
import io.neocore.api.host.login.ServerListPingEvent;
import io.neocore.api.player.NeoPlayer;
import io.neocore.bukkit.events.wrappers.BukkitInitialLoginEvent;
import io.neocore.bukkit.events.wrappers.BukkitPostJoinEvent;
import io.neocore.bukkit.events.wrappers.BukkitQuitEvent;
import io.neocore.bukkit.events.wrappers.BukkitServerPingEvent;
import io.neocore.common.player.CommonPlayerManager;

public class PlayerConnectionForwarder extends EventForwarder {
	
	public LoginAcceptor acceptor;
	private CommonPlayerManager manager;
	
	public PlayerConnectionForwarder(CommonPlayerManager man) {
		this.manager = man;
	}
	
	@EventHandler
	public void onPing(org.bukkit.event.server.ServerListPingEvent event) {
		
		BukkitServerPingEvent neoEvent = new BukkitServerPingEvent(event);
		
		// We don't need to do anything special for this so a standard broadcast event.
		NeocoreAPI.getAgent().getEventManager().broadcast(ServerListPingEvent.class, neoEvent); 
		
	}
	
	@EventHandler
	public void onPlayerLogin(PlayerLoginEvent event) {
		
		if (this.acceptor == null || !NeocoreAPI.getAgent().isInited()) {
			
			event.disallow(Result.KICK_OTHER, "Server still starting.  Try again.");
			return;
			
		}
		
		BukkitInitialLoginEvent neoEvent = new BukkitInitialLoginEvent(event);
		this.acceptor.onInitialLoginEvent(neoEvent);
		
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		
		if (this.acceptor == null) return;
		
		// Load the player data.
		NeoPlayer np = this.manager.assemblePlayer(event.getPlayer().getUniqueId());
		
		BukkitPostJoinEvent neoEvent = new BukkitPostJoinEvent(event, np);
		this.acceptor.onPostLoginEvent(neoEvent);
		
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		
		if (this.acceptor == null) return;
		
		NeoPlayer np = NeocoreAPI.getAgent().getPlayer(event.getPlayer().getUniqueId());
		BukkitQuitEvent neoEvent = new BukkitQuitEvent(event, np);
		this.acceptor.onDisconnectEvent(neoEvent);
		
		// The acceptor handles the destruction of the object.
		
	}
	
}
