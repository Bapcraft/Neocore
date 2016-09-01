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
	public T getPlayer(UUID uuid);
	
	/**
	 * Gets the player identity object from another identity object.
	 * 
	 * @param player The player identity we're using as the reference.
	 * @return The player identity of the requested type.
	 */
	public default T getPlayer(PlayerIdentity player) {
		return this.getPlayer(player.getUniqueId());
	}
	
}
