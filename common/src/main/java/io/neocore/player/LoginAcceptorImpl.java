package io.neocore.player;

import io.neocore.api.host.login.InitialLoginEvent;
import io.neocore.api.host.login.LoginAcceptor;
import io.neocore.api.host.login.PostLoginEvent;
import io.neocore.api.player.NeoPlayer;

public class LoginAcceptorImpl implements LoginAcceptor {
	
	private CommonPlayerManager manager;
	
	public LoginAcceptorImpl(CommonPlayerManager man) {
		this.manager = man;
	}
	
	@Override
	public void onInitialLoginEvent(InitialLoginEvent event) {
		
		// Load the player data.
		NeoPlayer np = this.manager.assemblePlayer(event.getPlayerUniqueId());
		
		// TODO Forward to event bus
		
		// Unload the player if necessary.
		if (!event.isPermitted()) this.manager.unloadPlayer(np);
		
	}

	@Override
	public void onPostLoginEvent(PostLoginEvent event) {
		// TODO Forward to event bus
	}

}
