package io.neocore.bungee.events;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import io.neocore.api.NeocoreAPI;
import io.neocore.api.database.player.DatabasePlayer;
import io.neocore.api.database.session.Session;
import io.neocore.api.database.session.SessionState;
import io.neocore.api.host.login.LoginAcceptor;
import io.neocore.api.host.permissions.PermissedPlayer;
import io.neocore.api.infrastructure.ProxyAcceptor;
import io.neocore.api.player.NeoPlayer;
import io.neocore.api.player.PlayerLease;
import io.neocore.api.player.PlayerManager;
import io.neocore.bungee.events.wrappers.BungeeInitialLoginEvent;
import io.neocore.bungee.events.wrappers.BungeePostLoginEvent;
import io.neocore.common.NeocoreImpl;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.event.ServerDisconnectEvent;
import net.md_5.bungee.event.EventHandler;

public class PlayerConnectionForwarder extends EventForwarder {
	
	private NeocoreImpl neocore;
	
	public LoginAcceptor loginAcceptor;
	public ProxyAcceptor proxyAcceptor;
	
	private Map<UUID, PlayerLease> leases = new ConcurrentHashMap<>();
	private Map<UUID, PlayerLease> disconnectingPlayers = new ConcurrentHashMap<>();
	
	public PlayerConnectionForwarder(NeocoreImpl neo) {
		this.neocore = neo;
	}
	
	@EventHandler
	public void onPreLogin(PreLoginEvent event) {
		
		/*
		 * If a player connects before the task that initializes Neocore
		 * quickly after starting the server then we need to initialize it
		 * ourselves.  If it's already being inited when when this call
		 * actually enters it will return immediately.
		 */
		this.neocore.init();
		
	}
	
	@EventHandler
	public void onLogin(LoginEvent event) {
		
		// Bungee doesn't give us a lot of information about players until now, so we do it now. 
		BungeeInitialLoginEvent ile = new BungeeInitialLoginEvent(event);
		if (this.loginAcceptor != null) this.loginAcceptor.onInitialLoginEvent(ile);
		
	}
	
	@EventHandler
	public void onPostLogin(PostLoginEvent event) {
		
		if (this.loginAcceptor == null) return;
		
		Logger log = NeocoreAPI.getLogger();
		
		// Now we can initialize the player.
		ProxiedPlayer player = event.getPlayer();
		UUID uuid = player.getUniqueId();
		
		PlayerManager pm = this.neocore.getPlayerManager();
		PlayerLease pl = pm.requestLease(uuid, loaded -> {
			
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
				sess.setNetworked(true);
				
				flush = true;
				
			}
			
			if (flush) loaded.flush();
			
			/*
			 * Now, we have to apply the permissions for the player.
			 */
			
			if (loaded.hasIdentity(DatabasePlayer.class) && loaded.hasIdentity(PermissedPlayer.class)) {
				this.neocore.getPermissionManager().assignPermissions(loaded);
			} else {
				log.warning("Not loading permissions for " + uuid + ".");
			}
			
		});
		
		// Store the lease.
		this.leases.put(uuid, pl);
		
		// Actually fire the event.
		if (this.loginAcceptor != null) this.loginAcceptor.onPostLoginEvent(new BungeePostLoginEvent(pl.getPlayer()));
		
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerDisconnectEvent event) {
		
		UUID uuid = event.getPlayer().getUniqueId();
		PlayerLease lease = this.leases.get(uuid);
		this.disconnectingPlayers.put(uuid, lease);
		
		// Now we call this to make sure the player is fully loaded before we try to unload it.
		try {
			lease.getPlayerEventually();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		NeocoreAPI.getLogger().fine("Registered a player lease of " + uuid + " for disconnection.");
		
	}
	
	@EventHandler
	public void onServerDisconnect(ServerDisconnectEvent event) {
		
		UUID uuid = event.getPlayer().getUniqueId();
		
		PlayerLease pl = this.disconnectingPlayers.get(uuid);
		if (pl != null) {
			
			this.neocore.getHost().getScheduler().invokeAsyncDelayed(() -> {
				
				NeoPlayer np = pl.getPlayer();
				
				if (np.hasIdentity(Session.class)) {
					
					Session sess = np.getSession();
					
					sess.setEndDate(new Date());
					sess.setState(SessionState.DISCONNECTED);
					
					np.dirty();
					
				}
				
				if (this.loginAcceptor != null) this.loginAcceptor.onDisconnectEvent(new BungeeQuitEvent(np));
				
				// Unloads should be flushed synchronously.
				np.flush();
				pl.release();
				
			}, 100L); // TODO Make configurable.
			
		} else {
			NeocoreAPI.getLogger().finer("Player moving server, don't need to worry about unloading just yet.");
		}
		
	}
	
	@EventHandler
	public void onDownstreamConnectEvent(ServerConnectEvent event) {
		
		NeoPlayer np = this.neocore.getPlayerManager().getPlayer(event.getPlayer().getUniqueId());
		
		BungeeDownstreamTransferEvent neoEvent = new BungeeDownstreamTransferEvent(np, event);
		if (this.proxyAcceptor != null) this.proxyAcceptor.onDownstreamTransfer(neoEvent);
		
	}
	
}
