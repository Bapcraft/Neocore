package io.neocore.bukkit.events;

import java.util.UUID;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerQuitEvent;

import com.treyzania.jzania.timing.TimeToken;
import com.treyzania.jzania.timing.Timer;

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
		
		TimeToken tt = Timer.getTimer().create("Login");
		
		if (this.acceptor == null || !NeocoreAPI.getAgent().isInited()) {
			
			event.disallow(Result.KICK_OTHER, "Server still starting.  Try again.");
			return;
			
		}
		
		tt.report("start check complete");
		
		BukkitInitialLoginEvent neoEvent = new BukkitInitialLoginEvent(event);
		this.acceptor.onInitialLoginEvent(neoEvent);
		
		tt.report("login event complete");
		
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		
		if (this.acceptor == null) return;
		
		TimeToken tt = Timer.getTimer().create("Join");
		
		UUID uuid = event.getPlayer().getUniqueId();
		
		// Initialize the player themselves.
		NeoPlayer np = this.manager.assemblePlayer(uuid, LoadType.FULL, loaded -> {
			
			tt.report("player assemble finish");
			
			loaded.setPopulated();
			this.eventManager.broadcast(PostLoadPlayerEvent.class, new PostLoadPlayerEvent(LoadReason.JOIN, loaded));
			
			tt.report("player assemble event broadcasted");
			
		});
		
		tt.report("player assemble init");
		
		BukkitPostJoinEvent neoEvent = new BukkitPostJoinEvent(event, np);
		this.acceptor.onPostLoginEvent(neoEvent);
		
		tt.report("player join finish");
		
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		
		if (this.acceptor == null) return;
		
		TimeToken tt = Timer.getTimer().create("Quit");
		
		NeoPlayer np = this.managerWrapper.getPlayer(event.getPlayer().getUniqueId());
		BukkitQuitEvent neoEvent = new BukkitQuitEvent(event, np);
		this.acceptor.onDisconnectEvent(neoEvent);
		
		tt.report("quit event broadcast");
		
		// The acceptor handles the destruction of the player.
		
	}
	
}
