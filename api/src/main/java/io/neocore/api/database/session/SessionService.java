package io.neocore.api.database.session;

import java.util.UUID;

import io.neocore.api.player.IdentityProvider;

public interface SessionService extends IdentityProvider<Session> {
	
	/**
	 * Gets the session for the player specified.  They do not have to be online.
	 * 
	 * @param uuid The UUID of the player.
	 * @return The session of the player.
	 */
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
	
	/**
	 * Flushes session data to the database.  If it is not present in the DB
	 * yet then a record is created.
	 * 
	 * @param session The session to store.
	 */
	public void flush(Session session);
	
	@Override
	public default Session getPlayer(UUID uuid) {
		return this.getSession(uuid);
	}
	
	/**
	 * Appends the specified movement of a player to the current session
	 * record for the player.  Does nothing if the player is not online.
	 * Should also update and cached ProxiedSession objects.
	 * 
	 * @param uuid The UUID of the player.
	 * @param move The movement record to append.
	 * @return <code>true</code> if the player is online, <code>false</code> otherwise.
	 */
	public boolean appendTransition(UUID uuid, EndpointMove move);
	
}
