package io.neocore.api.host.login;

import java.util.UUID;

import io.neocore.api.database.session.SimpleSessionImpl;
import io.neocore.api.host.HostServiceProvider;
import io.neocore.api.player.IdentityProvider;
import io.neocore.api.player.PlayerIdentity;

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
	
	/**
	 * Gets a newly-created session for this player.  Only called when the
	 * player has first connected to the server and we are the initial inbound
	 * connection.
	 * 
	 * @param uuid The player's UUID.
	 * @return The newly-created session.
	 */
	public SimpleSessionImpl initSession(UUID uuid);
	
	@Override
	default Class<? extends PlayerIdentity> getIdentityClass() {
		return ServerPlayer.class;
	}
	
}
