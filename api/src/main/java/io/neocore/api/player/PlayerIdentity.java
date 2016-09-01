package io.neocore.api.player;

import java.util.UUID;

/**
 * Represents a unique player.
 * 
 * @author treyzania
 */
public interface PlayerIdentity {
	
	/**
	 * Gets the universally-unique idetifier representing this player.
	 * 
	 * @return The player's UUID.
	 */
	public UUID getUniqueId();
	
}
