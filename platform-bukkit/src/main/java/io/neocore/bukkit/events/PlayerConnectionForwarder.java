package io.neocore.bukkit.events;

import java.util.UUID;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerQuitEvent;

import io.neocore.api.NeocoreAPI;
import io.neocore.api.event.EventManager;
import io.neocore.api.event.database.LoadReason;
import io.neocore.api.event.database.PostLoadPlayerEvent;
import io.neocore.api.host.login.LoginAcceptor;
import io.neocore.api.player.NeoPlayer;
import io.neocore.api.player.PlayerManager;
import io.neocore.bukkit.events.wrappers.BukkitInitialLoginEvent;
import io.neocore.bukkit.events.wrappers.BukkitPostJoinEvent;
import io.neocore.bukkit.events.wrappers.BukkitQuitEvent;
import io.neocore.bukkit.events.wrappers.BukkitServerPingEvent;
import io.neocore.common.player.CommonPlayerManager;
import io.neocore.common.player.LoadType;

public class PlayerConnectionForwarder extends EventForwarder {
	
	public LoginAcceptor acceptor;
	private PlayerManager managerWrapper;
	private CommonPlayerManager manager;
	private EventManager eventManager;
	
	public PlayerConnectionForwarder(PlayerManager wrap, CommonPlayerManager man) {
		
		this.managerWrapper = wrap;
		this.manager = man;
		
	}
	
	@EventHandler
	public void onPing(org.bukkit.event.server.ServerListPingEvent event) {
		
		// We don't need to do anything special for this so a standard broadcast event.
		NeocoreAPI.getAgent().getEventManager().broadcast(new BukkitServerPingEvent(event)); 
		
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
		
		UUID uuid = event.getPlayer().getUniqueId();
		
		// Initialize the player themselves.
		NeoPlayer np = this.manager.assemblePlayer(uuid, LoadType.FULL, loaded -> {
			
			loaded.setPopulated();
			this.eventManager.broadcast(new PostLoadPlayerEvent(LoadReason.JOIN, loaded));
			
		});
		
		BukkitPostJoinEvent neoEvent = new BukkitPostJoinEvent(event, np);
		this.acceptor.onPostLoginEvent(neoEvent);
		
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		
		if (this.acceptor == null) return;
		
		NeoPlayer np = this.managerWrapper.getPlayer(event.getPlayer().getUniqueId());
		BukkitQuitEvent neoEvent = new BukkitQuitEvent(event, np);
		this.acceptor.onDisconnectEvent(neoEvent);
		
		// The acceptor handles the destruction of the player.
		
	}
	
}
