package io.neocore.bungee.services;

import java.util.UUID;

import io.neocore.api.host.login.LoginAcceptor;
import io.neocore.api.host.login.LoginService;
import io.neocore.api.host.login.ServerPlayer;
import io.neocore.bungee.events.PlayerConnectionForwarder;

public class BungeeLoginService implements LoginService {
	
	private PlayerConnectionForwarder forwarder;
	
	public BungeeLoginService(PlayerConnectionForwarder fwdr) {
		this.forwarder = fwdr;
	}

	@Override
	public ServerPlayer load(UUID uuid) {
		return new BungeePlayer(uuid);
	}
	
	@Override
	public void setLoginAcceptor(LoginAcceptor acceptor) {
		this.forwarder.loginAcceptor = acceptor;
	}
	
	@Override
	public LoginAcceptor getLoginAcceptor() {
		return this.forwarder.loginAcceptor;
	}
	
}
