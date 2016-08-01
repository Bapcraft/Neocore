package io.neocore.player;

import io.neocore.api.host.login.DisconnectEvent;
import io.neocore.api.host.login.InitialLoginEvent;
import io.neocore.api.host.login.LoginAcceptor;
import io.neocore.api.host.login.PostLoginEvent;
import io.neocore.api.player.NeoPlayer;
import io.neocore.event.CommonEventManager;

public class LoginAcceptorImpl implements LoginAcceptor {
	
	private CommonPlayerManager players;
	private CommonEventManager events;
	
	public LoginAcceptorImpl(CommonPlayerManager players, CommonEventManager events) {
		
		this.players = players;
		this.events = events;
		
	}
	
	@Override
	public void onInitialLoginEvent(InitialLoginEvent event) {
		
		// Load the player data.
		NeoPlayer np = this.players.assemblePlayer(event.getPlayerUniqueId());
		
		// Broadcast the event.
		this.events.broadcast(event);
		
		// Unload the player if necessary.
		if (!event.isPermitted()) this.players.unloadPlayer(np);
		
	}

	@Override
	public void onPostLoginEvent(PostLoginEvent event) {
		this.events.broadcast(event);
	}

	@Override
	public void onDisconnectEvent(DisconnectEvent event) {
		
		// Broadcast the event straightaway.
		this.events.broadcast(event);
		
		// Unload once we're sure everyone is done using it.
		this.players.unloadPlayer(event.getPlayer());
		
	}

}
