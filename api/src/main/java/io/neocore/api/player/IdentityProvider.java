package io.neocore.api.player;

import java.util.UUID;
import java.util.function.Consumer;

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
	 * Asynchronously gets the player data for the player.
	 * 
	 * @param uuid The UUID of the player.
	 * @param callback The callback to stuff the player data.
	 */
	public default void getPlayer(UUID uuid, Consumer<T> callback) {
		callback.accept(this.getPlayer(uuid));
	}
	
	/**
	 * Gets the player identity object from another identity object.
	 * 
	 * @param player The player identity we're using as the reference.
	 * @return The player identity of the requested type.
	 */
	public default T getPlayer(PlayerIdentity player) {
		return this.getPlayer(player.getUniqueId());
	}
	
	/**
	 * Unloads the player data from the record.
	 * 
	 * @param uuid The player UUID.
	 */
	public default void unload(UUID uuid) {
		
	}
	
	/**
	 * Asynchronously unloads the player data.
	 * 
	 * @param uuid The player's UUID.
	 * @param callback The callback to run once completed.
	 */
	public default void unload(UUID uuid, Runnable callback) {
		
		this.unload(uuid);
		callback.run();
		
	}
	
	/**
	 * Unloads the player data from the record.
	 * 
	 * @param player The player UUID.
	 */
	public default void unload(PlayerIdentity player) {
		this.unload(player.getUniqueId());
	}
	
}
