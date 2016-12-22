package io.neocore.bungee.events;

import java.util.Date;
import java.util.UUID;

import io.neocore.api.NeocoreAPI;
import io.neocore.api.database.player.DatabasePlayer;
import io.neocore.api.database.session.Session;
import io.neocore.api.database.session.SessionState;
import io.neocore.api.event.database.LoadReason;
import io.neocore.api.host.login.LoginAcceptor;
import io.neocore.api.host.permissions.PermissedPlayer;
import io.neocore.api.infrastructure.ProxyAcceptor;
import io.neocore.api.player.NeoPlayer;
import io.neocore.bungee.events.wrappers.BungeeInitialLoginEvent;
import io.neocore.bungee.events.wrappers.BungeePostLoginEvent;
import io.neocore.common.NeocoreImpl;
import io.neocore.common.player.CommonPlayerManager;
import io.neocore.common.player.LoadType;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.event.EventHandler;

public class PlayerConnectionForwarder extends EventForwarder {
	
	private NeocoreImpl neocore;
	
	public LoginAcceptor loginAcceptor;
	public ProxyAcceptor proxyAcceptor;
	
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
		
		// Now we can initialize the player.
		ProxiedPlayer player = event.getPlayer();
		UUID uuid = player.getUniqueId();
		
		CommonPlayerManager cpm = this.neocore.getPlayerAssembler();
		NeoPlayer np = cpm.assemblePlayer(uuid, LoadReason.JOIN, LoadType.FULL, loaded -> {
			

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
		
		// Actually fire the event.
		if (this.loginAcceptor != null) this.loginAcceptor.onPostLoginEvent(new BungeePostLoginEvent(np));
		
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerDisconnectEvent event) {
		
		NeoPlayer np = this.neocore.getPlayerManager().getPlayer(event.getPlayer().getUniqueId());
		if (np.hasIdentity(Session.class)) {
			
			Session sess = np.getSession();
			
			sess.setEndDate(new Date());
			sess.setState(SessionState.DISCONNECTED);
			
			// We don't flush it because it'll do that for unloading, just make sure it's dirty.
			np.dirty();
			
		}
		
		if (this.loginAcceptor != null) this.loginAcceptor.onDisconnectEvent(new BungeeQuitEvent(np));
		
	}
	
	@EventHandler
	public void onDownstreamConnectEvent(ServerConnectEvent event) {
		
		NeoPlayer np = this.neocore.getPlayerManager().getPlayer(event.getPlayer().getUniqueId());
		
		BungeeDownstreamTransferEvent neoEvent = new BungeeDownstreamTransferEvent(np, event);
		if (this.proxyAcceptor != null) this.proxyAcceptor.onDownstreamTransfer(neoEvent);
		
	}
	
}
