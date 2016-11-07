package io.neocore.api.database.session;

import java.util.UUID;

import io.neocore.api.LoadAsync;
import io.neocore.api.database.DatabaseServiceProvider;
import io.neocore.api.database.IdentityLinkage;
import io.neocore.api.player.PlayerIdentity;

@LoadAsync
public interface SessionService extends DatabaseServiceProvider, IdentityLinkage<Session> {
	
	/**
	 * Gets the session for the player specified.  They do not have to be online.
	 * 
	 * @param uuid The UUID of the player.
	 * @return The session of the player.
	 */
	public Session getSession(UUID uuid);
	
	@Override
	public default Session load(UUID uuid) {
		return this.getSession(uuid);
	}
	
	@Override
	default Class<? extends PlayerIdentity> getIdentityClass() {
		return Session.class;
	}
	
}
