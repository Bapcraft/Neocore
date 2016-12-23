package io.neocore.common.player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.neocore.api.NeocoreAPI;
import io.neocore.api.database.artifact.IdentifierManager;
import io.neocore.api.database.ban.BanEntry;
import io.neocore.api.database.ban.BanService;
import io.neocore.api.database.player.DatabasePlayer;
import io.neocore.api.event.database.FlushReason;
import io.neocore.api.event.database.PostUnloadPlayerEvent;
import io.neocore.api.event.database.UnloadReason;
import io.neocore.api.host.Context;
import io.neocore.api.host.login.DisconnectEvent;
import io.neocore.api.host.login.InitialLoginEvent;
import io.neocore.api.host.login.LoginAcceptor;
import io.neocore.api.host.login.LoginService;
import io.neocore.api.host.login.PostLoginEvent;
import io.neocore.api.player.NeoPlayer;
import io.neocore.common.event.CommonEventManager;
import io.neocore.common.service.ServiceManagerImpl;

public class LoginAcceptorImpl implements LoginAcceptor {
	
	private CommonPlayerManager players;
	private CommonEventManager events;
	private ServiceManagerImpl services;
	private IdentifierManager idents;
	
	private List<Context> contexts;
	
	public LoginAcceptorImpl(CommonPlayerManager players, CommonEventManager events, ServiceManagerImpl services, IdentifierManager idents, List<Context> ctxs) {
		
		this.players = players;
		this.events = events;
		this.services = services;
		this.idents = idents;
		
		this.contexts = ctxs;
		
		this.services.registerRegistrationHandler(LoginService.class, r -> ((LoginService) r.getServiceProvider()).setLoginAcceptor(this));
		
	}
	
	@Override
	public void onInitialLoginEvent(InitialLoginEvent event) {
		
		UUID uuid = event.getPlayerUniqueId();
		
		// Verify bans
		if (NeocoreAPI.getAgent().getHost().getNeocoreConfig().isEnforcingBans()) { // FIXME Encapsulation.
			
			BanService serv = this.services.getService(BanService.class);
			if (serv != null) {
				
				// Figure out which bans we care about.
				List<BanEntry> playerBans = serv.getBans(uuid);
				List<BanEntry> relevant = new ArrayList<>();
				for (BanEntry ban : playerBans) {
					
					// Need to make sure that we can have temporary bans.
					if (!ban.isActive()) continue;
					
					if (ban.isGlobal()) {
						
						relevant.add(ban);
						continue;
						
					} else {
						
						for (Context c : this.contexts) {
							
							if (c.equals(ban.getContext())) {
								
								relevant.add(ban);
								continue;
								
							}
							
						}
						
					}
					
				}
				
				// Now report to the player if they're banned, and why.
				if (relevant.size() > 0) {
					
					NeocoreAPI.getLogger().fine("Found " + relevant.size() + " relevant bans for player logging in.");
					
					StringBuilder sb = new StringBuilder("Banned!\n");
					relevant.forEach(b -> sb.append("- " + b.getReason() + " (" + this.idents.getIdentifier(b.getIssuerId()) + ")\n"));
					event.disallow(sb.toString());
					
				} else {
					event.allow(); // Verbosity.
				}
				
			} else {
				NeocoreAPI.getLogger().warning("Ban service null but we're supposed to be checking bans!");
			}
			
		}
		
		// Broadcast the event.
		this.events.broadcast(event);
		
		/**
		 * We can't do any player initialization things here because Bukkit is dumb.
		 */
		
	}
	
	@Override
	public void onPostLoginEvent(PostLoginEvent event) {
		
		NeoPlayer np = event.getPlayer();
		
		// Initialize the player username stuff.
		if (np.hasIdentity(DatabasePlayer.class)) {
			
			DatabasePlayer dbp = np.getIdentity(DatabasePlayer.class);
			dbp.setLastUsername(np.getUsername());
			
		}
		
		// Broadcast
		this.events.broadcast(PostLoginEvent.class, event);
		
	}
	
	@Override
	public void onDisconnectEvent(DisconnectEvent event) {
		
		NeoPlayer np = event.getPlayer();
		
		// Broadcast the event straightaway.
		this.events.broadcast(event);
		
		// Setup the unload and broadcast.
		Runnable unloadAndBroadcast = () -> {
			
			this.players.unloadPlayer(np.getUniqueId(), UnloadReason.DISCONNECT, () -> {
				this.events.broadcast(new PostUnloadPlayerEvent(UnloadReason.DISCONNECT, np.getUniqueId()));
			});
			
		};
		
		// Now we (flush and) unload once we're sure everyone is done using it.
		if (np.isDirty()) {
			
			// Either do the unload after a flush...
			this.players.flushPlayer(np.getUniqueId(), FlushReason.UNLOAD, unloadAndBroadcast);
			
		} else {
			
			/// ...or do immediately.
			unloadAndBroadcast.run();
			
		}
		
	}
	
}
