package io.neocore.host.login;

import java.util.UUID;

import io.neocore.host.HostServiceProvider;

public interface LoginProvider extends HostServiceProvider {
	
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
