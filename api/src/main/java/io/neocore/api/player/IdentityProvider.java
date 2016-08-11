package io.neocore.api.player;

import java.util.UUID;

public interface IdentityProvider<T extends PlayerIdentity> {
	
	/**
	 * Gets the player identity object from the specified UUID
	 * 
	 * @param uuid The UUID of the player
	 * @return The corresponding identity
	 */
	public T getPlayer(UUID uuid);
	
	public default T getPlayer(PlayerIdentity player) {
		return this.getPlayer(player.getUniqueId());
	}
	
}
