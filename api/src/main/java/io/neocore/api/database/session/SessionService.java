package io.neocore.api.database.session;

import java.util.UUID;
import java.util.function.Consumer;

import io.neocore.api.database.DatabaseServiceProvider;
import io.neocore.api.database.IdentityLinkage;
import io.neocore.api.database.LoadAsync;
import io.neocore.api.player.PlayerIdentity;

@LoadAsync
public interface SessionService extends DatabaseServiceProvider, IdentityLinkage<SimpleSessionImpl> {
	
	/**
	 * Gets the session for the player specified.  They do not have to be online.
	 * 
	 * @param uuid The UUID of the player.
	 * @return The session of the player.
	 */
	public SimpleSessionImpl getSession(UUID uuid);
	
	@Override
	public default SimpleSessionImpl load(UUID uuid) {
		return this.getSession(uuid);
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
	public void appendTransition(UUID uuid, EndpointMoveImpl move, Consumer<Boolean> callback);

	@Override
	default Class<? extends PlayerIdentity> getIdentityClass() {
		return SimpleSessionImpl.class;
	}
	
}
