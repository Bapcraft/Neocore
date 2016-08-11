package io.neocore.api.host.login;

import io.neocore.api.host.HostServiceProvider;
import io.neocore.api.player.IdentityProvider;

public interface LoginService extends HostServiceProvider, IdentityProvider<ServerPlayer> {
	
	public void setLoginAcceptor(LoginAcceptor acceptor);
	public LoginAcceptor getLoginAcceptor();
	
}
