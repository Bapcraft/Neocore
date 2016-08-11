package io.neocore.api.host.login;

import java.util.UUID;

import io.neocore.api.host.HostServiceProvider;

public interface LoginService extends HostServiceProvider {
	
	public void setLoginAcceptor(LoginAcceptor acceptor);
	public LoginAcceptor getLoginAcceptor();
	
	/**
	 * Gets the player agent information about the specified player.
	 * 
	 * @param uuid The UUID of the player
	 * @return The player agent object
	 */
	public ServerPlayer getPlayer(UUID uuid);
	
}
