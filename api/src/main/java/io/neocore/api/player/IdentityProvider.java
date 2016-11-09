package io.neocore.api.player;

import java.util.UUID;

/**
 * Provides an object representing some kind of property about the player, to
 * be stored in an object of type T.
 * 
 * @author treyzania
 *
 * @param <T> The type of object storing the player's information.
 */
public interface IdentityProvider<T extends PlayerIdentity> {
	
	/**
	 * Synchronously gets the player identity object from the specified UUID,
	 * loading from the database if necessary.
	 * 
	 * @param uuid The UUID of the player
	 * @return The corresponding identity
	 */
	public T load(UUID uuid);
	
	/**
	 * Purges player data from any local caches without writing anything to
	 * backends.
	 * 
	 * @param uuid The player UUID.
	 */
	public default void unload(UUID uuid) {
		
	}
	
	public Class<? extends PlayerIdentity> getIdentityClass();
	
}
