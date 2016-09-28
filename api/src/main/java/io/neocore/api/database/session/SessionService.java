package io.neocore.api.database.session;

import java.util.UUID;
import java.util.function.Consumer;

import io.neocore.api.database.DatabaseServiceProvider;
import io.neocore.api.player.IdentityProvider;

public interface SessionService extends DatabaseServiceProvider, IdentityProvider<Session> {
	
	/**
	 * Gets the session for the player specified.  They do not have to be online.
	 * 
	 * @param uuid The UUID of the player.
	 * @return The session of the player.
	 */
	public Session getSession(UUID uuid);
	
	/**
	 * Asynchronously gets the player's session from the database.
	 * 
	 * @param uuid The UUID of the player.
	 * @param callback The callback to stuff the player session.
	 */
	public default void getSession(UUID uuid, Consumer<Session> callback) {
		callback.accept(this.getSession(uuid));
	}
	
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
	
	/**
	 * Asynchronously flushes the session data to the database.  If not present
	 * in the DB yet then a record is created.
	 * 
	 * @param session The session to store.
	 * @param callback The callback to run once completed.
	 */
	public default void flush(Session session, Runnable callback) {
		
		this.flush(session);
		callback.run();
		
	}
	
	@Override
	public default Session getPlayer(UUID uuid) {
		return this.getSession(uuid);
	}
	
	@Override
	default void getPlayer(UUID uuid, Consumer<Session> callback) {
		this.getSession(uuid, callback);
	}

	/**
	 * Asynchronously appends the specified movement of a player to the current
	 * session record for the player.  Does nothing if the player is not
	 * online. Should also update and cached ProxiedSession objects.
	 * 
	 * @param uuid The UUID of the player.
	 * @param move The movement record to append.
	 * @param callback What to call once completed.  <code>true</code> if the
	 * 			player is online, <code>false</code> otherwise.
	 */
	public void appendTransition(UUID uuid, EndpointMove move, Consumer<Boolean> callback);
	
}
