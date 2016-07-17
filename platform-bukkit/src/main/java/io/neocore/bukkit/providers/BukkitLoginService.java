package io.neocore.bukkit.providers;

import java.util.UUID;

import io.neocore.api.host.login.LoginAcceptor;
import io.neocore.api.host.login.LoginProvider;
import io.neocore.api.host.login.ServerPlayer;

public class BukkitLoginService implements LoginProvider {
	
	private LoginAcceptor acceptor;
	
	@Override
	public void setLoginAcceptor(LoginAcceptor acceptor) {
		this.acceptor = acceptor;
	}

	@Override
	public LoginAcceptor getLoginAcceptor() {
		return this.acceptor;
	}

	@Override
	public ServerPlayer getPlayer(UUID uuid) {
		// TODO Auto-generated method stub
		return null;
	}

}
