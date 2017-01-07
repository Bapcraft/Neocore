package io.neocore.bungee.services;

import java.util.UUID;

import io.neocore.api.host.login.LoginAcceptor;
import io.neocore.api.host.login.LoginService;
import io.neocore.api.host.login.ServerPlayer;
import io.neocore.bungee.events.PlayerConnectionForwarder;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

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

	@Override
	public ServerPlayer findPlayerByName(String name) {
		
		ProxiedPlayer pp = ProxyServer.getInstance().getPlayer(name);
		
		if (pp != null) {
			return this.load(pp.getUniqueId());
		} else {
			return null;
		}
		
	}
	
}
