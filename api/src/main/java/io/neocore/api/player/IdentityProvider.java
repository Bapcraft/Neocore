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
	 * Gets the player identity object from the specified UUID.
	 * 
	 * @param uuid The UUID of the player
	 * @return The corresponding identity
	 */
	public T load(UUID uuid);
	
	/**
	 * Unloads the player data from the record and clears any caches, does not
	 * save anything to any databases.
	 * 
	 * @param uuid The player UUID.
	 */
	public default void unload(UUID uuid) {
		
	}
	
}
