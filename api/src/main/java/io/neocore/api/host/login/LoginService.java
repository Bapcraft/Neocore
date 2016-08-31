package io.neocore.api.host.login;

import io.neocore.api.host.HostServiceProvider;
import io.neocore.api.player.IdentityProvider;

public interface LoginService extends HostServiceProvider, IdentityProvider<ServerPlayer> {
	
	/**
	 * Sets the login acceptor.
	 * 
	 * @param acceptor The acceptor.
	 */
	public void setLoginAcceptor(LoginAcceptor acceptor);
	
	/**
	 * Gets the current login acceptor.
	 * 
	 * @return The acceptor.
	 */
	public LoginAcceptor getLoginAcceptor();
	
}
