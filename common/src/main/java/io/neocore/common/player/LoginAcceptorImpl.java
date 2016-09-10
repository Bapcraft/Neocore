package io.neocore.common.player;

import java.sql.Date;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import io.neocore.api.NeocoreAPI;
import io.neocore.api.database.ban.BanList;
import io.neocore.api.database.ban.BanService;
import io.neocore.api.database.session.Session;
import io.neocore.api.database.session.SessionState;
import io.neocore.api.host.Context;
import io.neocore.api.host.login.DisconnectEvent;
import io.neocore.api.host.login.InitialLoginEvent;
import io.neocore.api.host.login.LoginAcceptor;
import io.neocore.api.host.login.PostLoginEvent;
import io.neocore.api.player.NeoPlayer;
import io.neocore.common.event.CommonEventManager;
import io.neocore.common.service.ServiceManagerImpl;

public class LoginAcceptorImpl implements LoginAcceptor {
	
	private CommonPlayerManager players;
	private CommonEventManager events;
	private ServiceManagerImpl services;
	
	private List<Context> contexts;
	
	public LoginAcceptorImpl(CommonPlayerManager players, CommonEventManager events, ServiceManagerImpl services, List<Context> ctxs) {
		
		this.players = players;
		this.events = events;
		this.services = services;
		
		this.contexts = ctxs;
		
	}
	
	@Override
	public void onInitialLoginEvent(InitialLoginEvent event) {
		
		UUID uuid = event.getPlayerUniqueId();
		
		// Verify bans
		// TODO Flesh this out a lot to actually report the reasons and make the messages more configurable.
		BanList playerBans = this.services.getService(BanService.class).getBans(uuid);
		for (Context ctx : this.contexts) {
			
			if (playerBans.isBannedInContext(ctx)) {
				
				event.disallow("You're banned!");
				return;
				
			}
			
		}
		
		// Broadcast the event.
		this.events.broadcast(InitialLoginEvent.class, event);
		
	}
	
	@Override
	public void onPostLoginEvent(PostLoginEvent event) {
		this.events.broadcast(PostLoginEvent.class, event);
	}
	
	@Override
	public void onDisconnectEvent(DisconnectEvent event) {
		
		NeoPlayer np = event.getPlayer();
		
		// Clear session data.
		if (NeocoreAPI.isFrontend()) {
			
			Session sess = np.getSession();
			sess.setState(SessionState.DISCONNECTED);
			sess.setEndDate(Date.from(Instant.now()));
			
		}
		
		// Broadcast the event straightaway.
		this.events.broadcast(DisconnectEvent.class, event);
		
		// Unload once we're sure everyone is done using it.
		this.players.unloadPlayer(np);
		
	}
	
}
