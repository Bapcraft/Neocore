package io.neocore.bukkit.events;

import java.util.Date;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerQuitEvent;

import io.neocore.api.NeocoreAPI;
import io.neocore.api.database.player.DatabasePlayer;
import io.neocore.api.database.session.ProxiedSession;
import io.neocore.api.database.session.Session;
import io.neocore.api.database.session.SessionState;
import io.neocore.api.event.database.LoadReason;
import io.neocore.api.host.login.LoginAcceptor;
import io.neocore.api.host.permissions.PermissedPlayer;
import io.neocore.api.player.NeoPlayer;
import io.neocore.bukkit.events.wrappers.BukkitInitialLoginEvent;
import io.neocore.bukkit.events.wrappers.BukkitPostJoinEvent;
import io.neocore.bukkit.events.wrappers.BukkitQuitEvent;
import io.neocore.bukkit.events.wrappers.BukkitServerPingEvent;
import io.neocore.common.NeocoreImpl;
import io.neocore.common.player.LoadType;

public class PlayerConnectionForwarder extends EventForwarder {
	
	private NeocoreImpl neocore;
	
	public LoginAcceptor acceptor;
	
	public PlayerConnectionForwarder(NeocoreImpl neo) {
		this.neocore = neo;
	}
	
	@EventHandler
	public void onPing(org.bukkit.event.server.ServerListPingEvent event) {
		
		// We don't need to do anything special for this so a standard broadcast event.
		NeocoreAPI.getAgent().getEventManager().broadcast(new BukkitServerPingEvent(event)); 
		
	}
	
	@EventHandler
	public void onPlayerLogin(PlayerLoginEvent event) {
		
		/*
		 * If a player connects before the task that initializes Neocore
		 * quickly after starting the server then we need to initialize it
		 * ourselves.  If it's already being inited when when this call
		 * actually enters it will return immediately.
		 */
		this.neocore.init();
		
		// Now we actually do the Neocore logic.
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
		
		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();
		
		// Initialize the player themselves.
		NeoPlayer np = this.neocore.getPlayerAssembler().assemblePlayer(uuid, LoadReason.JOIN, LoadType.FULL, loaded -> {
			
			/*
			 * First we increase the login count and update the last login, and
			 * then we deal with the session stuff. 
			 */
			
			boolean flush = false;
			
			// Handle the general player data.
			if (NeocoreAPI.isFrontend() && loaded.hasIdentity(DatabasePlayer.class)) {
				
				DatabasePlayer dbp = loaded.getIdentity(DatabasePlayer.class);
				
				// Potentially update the name.
				String playerName = player.getName();
				if (!playerName.equals(dbp.getLastUsername())) dbp.setLastUsername(playerName);
				
				// Update the last login.
				dbp.setLastLogin(new Date());
				
				// Update the login count.
				dbp.setLoginCount(dbp.getLoginCount() + 1);
				
				flush = true;
				
			}
			
			// Handle setting data.
			if (NeocoreAPI.isFrontend() && loaded.hasIdentity(Session.class)) {
				
				Session sess = loaded.getIdentity(Session.class);
				
				// Update all the fancy values.
				sess.setLoginUsername(player.getName());
				sess.setStartDate(new Date());
				sess.setState(SessionState.ACTIVE);
				sess.setFrontend(NeocoreAPI.getServerName());
				
				// Update network info.
				sess.setAddress(player.getAddress().getAddress());
				sess.setHostString(player.getAddress().getHostName());
				
				if (NeocoreAPI.isNetworked()) {
					
					ProxiedSession ps = sess.getAsProxiedSession();
					ps.setNetworked(true);
					
				}
				
				flush = true;
				
			}
			
			if (flush) loaded.flush();
			
			/*
			 * Now, we have to apply the permissions for the player.
			 */
			
			if (loaded.hasIdentity(DatabasePlayer.class) && loaded.hasIdentity(PermissedPlayer.class)) {
				this.neocore.getPermissionManager().assignPermissions(loaded);
			}
			
		});
		
		BukkitPostJoinEvent neoEvent = new BukkitPostJoinEvent(event, np);
		this.acceptor.onPostLoginEvent(neoEvent);
		
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		
		if (this.acceptor == null) return;
		
		Player p = event.getPlayer();
		NeocoreAPI.getLogger().fine("Got quit event for player " + p.getName() + ", UUID " + p.getUniqueId() + ".");
		
		NeoPlayer np = this.neocore.getPlayerManager().getPlayer(p.getUniqueId());
		
		if (np == null) {
			
			NeocoreAPI.getLogger().severe("Unloading player " + p.getName() + " (" + p.getUniqueId() + ") not possible, no NeoPlayer found!");
			return;
			
		}
		
		// Set the session state to disconnected.
		if (NeocoreAPI.isFrontend()) {
			
			if (np.hasIdentity(Session.class)) {
				
				Session sess = np.getSession();
				
				sess.setEndDate(new Date());
				sess.setState(SessionState.DISCONNECTED);
				
				// We don't flush it because it'll do that for unloading, just make sure it's dirty.
				np.dirty();
				
			}
			
		}
		
		BukkitQuitEvent neoEvent = new BukkitQuitEvent(event, np);
		this.acceptor.onDisconnectEvent(neoEvent);
		
		// The acceptor handles the destruction of the player.
		
	}
	
}
