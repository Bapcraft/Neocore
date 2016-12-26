package io.neocore.bukkit.services;

import java.util.UUID;

import io.neocore.api.host.login.LoginAcceptor;
import io.neocore.api.host.login.LoginService;
import io.neocore.api.host.login.ServerPlayer;
import io.neocore.bukkit.BukkitPlayer;
import io.neocore.bukkit.events.PlayerConnectionForwarder;

public class BukkitLoginService implements LoginService {
	
	private PlayerConnectionForwarder forwarder;
	
	public BukkitLoginService(PlayerConnectionForwarder fwdr) {
		this.forwarder = fwdr;
	}
	
	@Override
	public void setLoginAcceptor(LoginAcceptor acceptor) {
		this.forwarder.acceptor = acceptor;
	}
	
	@Override
	public LoginAcceptor getLoginAcceptor() {
		return this.forwarder.acceptor;
	}
	
	@Override
	public ServerPlayer load(UUID uuid) {
		return new BukkitPlayer(uuid);
	}
	
}
