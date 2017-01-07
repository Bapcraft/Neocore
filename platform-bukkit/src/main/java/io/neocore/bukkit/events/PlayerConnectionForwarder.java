package io.neocore.bukkit.events;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.permissions.PermissibleBase;

import com.treyzania.jzania.reflect.FieldWrapper;

import org.bukkit.event.player.PlayerQuitEvent;

import io.neocore.api.NeocoreAPI;
import io.neocore.api.database.player.DatabasePlayer;
import io.neocore.api.database.session.ProxiedSession;
import io.neocore.api.database.session.Session;
import io.neocore.api.database.session.SessionState;
import io.neocore.api.host.chat.ChattablePlayer;
import io.neocore.api.host.login.LoginAcceptor;
import io.neocore.api.host.permissions.PermissedPlayer;
import io.neocore.api.player.NeoPlayer;
import io.neocore.api.player.PlayerLease;
import io.neocore.api.player.PlayerManager;
import io.neocore.api.player.group.Flair;
import io.neocore.bukkit.events.wrappers.BukkitInitialLoginEvent;
import io.neocore.bukkit.events.wrappers.BukkitPostJoinEvent;
import io.neocore.bukkit.events.wrappers.BukkitQuitEvent;
import io.neocore.bukkit.events.wrappers.BukkitServerPingEvent;
import io.neocore.bukkit.services.permissions.NeocorePermissibleBase;
import io.neocore.common.NeocoreImpl;
import net.md_5.bungee.api.ChatColor;

public class PlayerConnectionForwarder extends EventForwarder {
	
	private NeocoreImpl neocore;
	
	public LoginAcceptor acceptor;
	
	private Map<UUID, PlayerLease> leases = new ConcurrentHashMap<>();
	
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
		PlayerManager pm = this.neocore.getPlayerManager();
		PlayerLease pl = pm.requestLease(uuid, loaded -> this.processPostLoad(loaded));
		
		this.leases.put(uuid, pl);
		
		BukkitPostJoinEvent neoEvent = new BukkitPostJoinEvent(event, pl.getPlayer());
		this.acceptor.onPostLoginEvent(neoEvent);
		
	}
	
	private void processPostLoad(NeoPlayer loaded) {
		
		Logger log = NeocoreAPI.getLogger();
		
		UUID uuid = loaded.getUniqueId();
		Player player = Bukkit.getPlayer(uuid);
		
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
		 * Now, we have to apply the permissions and set up display name.
		 */
		
		if (loaded.hasIdentity(DatabasePlayer.class) && loaded.hasIdentity(PermissedPlayer.class)) {
			
			try {
				
				FieldWrapper<Player, PermissibleBase> fw = new FieldWrapper<>(player.getClass(), "perm");
				
				NeocorePermissibleBase override = new NeocorePermissibleBase(player);
				fw.set(player, override);
				
				NeocoreAPI.getLogger().finer("Injected new PermissibleBase.");
				
			} catch (NoSuchFieldException e) {
				NeocoreAPI.getLogger().log(Level.SEVERE, "Couldn't inject NeocorePermissibleBase.", e);
			} catch (SecurityException e) {
				NeocoreAPI.getLogger().log(Level.SEVERE, "Couldn't inject NeocorePermissibleBase.", e);
			}
			
			try {
				
				// this should throw an UnsupportedOperationException if we did it right.
				player.hasPermission("!test");
				throw new IllegalStateException("PermissibleBase could not be injected!");
				
			} catch (UnsupportedOperationException e) {
				
				// this is supposed to happen
				log.finer("Injection test passed for " + player + ".");
				
			}
			
			this.neocore.getPermissionManager().assignPermissions(loaded);
			log.fine("Populated permissions.");
			
		} else {
			log.warning("Not loading permissions for " + uuid + ".");
		}
		
		if (loaded.hasIdentity(DatabasePlayer.class) && loaded.hasIdentity(ChattablePlayer.class)) {
			
			DatabasePlayer dbp = loaded.getIdentity(DatabasePlayer.class);
			ChattablePlayer cp = loaded.getIdentity(ChattablePlayer.class);
			
			Flair f = dbp.getCurrentFlair();
			if (f != null) cp.setDisplayName(ChatColor.translateAlternateColorCodes('&', f.apply(player.getName())));
			
		} else {
			log.warning("Not setting flair on " + uuid + ".");
		}
		
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		
		if (this.acceptor == null) return;
		
		Player p = event.getPlayer();
		NeocoreAPI.getLogger().fine("Got quit event for player " + p.getName() + ", UUID " + p.getUniqueId() + ".");
		
		PlayerLease pl = this.leases.get(p.getUniqueId());
		NeoPlayer np = pl.getPlayer();
		
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
		
		pl.release();
		
	}
	
}
