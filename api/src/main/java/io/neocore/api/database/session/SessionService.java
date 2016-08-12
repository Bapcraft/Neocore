package io.neocore.api.database.session;

import java.util.UUID;

import io.neocore.api.player.IdentityProvider;

public interface SessionService extends IdentityProvider<Session> {
	
	public Session getSession(UUID uuid);
	
	/**
	 * Reloads the sessions in the cache from the database.
	 */
	public void updateSessions();
	
	/**
	 * Purges the session from the cache allowing it to be GCed.
	 * 
	 * @param session The session to purge.
	 */
	public void unload(Session session);
	
	@Override
	public default Session getPlayer(UUID uuid) {
		return this.getSession(uuid);
	}
	
}
