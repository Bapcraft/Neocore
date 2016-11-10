package io.neocore.common.player;

import java.util.Set;
import java.util.UUID;

import io.neocore.api.database.PersistentPlayerIdentity;

public abstract class NetworkSync {
	
	/**
	 * Potentially announces an update in the subscription state for the given
	 * unique ID.  If the state is essentially unchanged, then nothing is
	 * announced.  This may flip flop rapidly in some cases, so plan to handle
	 * traffic congestion accordingly.
	 * 
	 * @param uuid The UUID of the player.
	 * @param state The "new" state.
	 */
	public abstract void updateSubscriptionState(UUID uuid, boolean state);
	
	/**
	 * Announces the player list on the server, effectively overriding all
	 * previous state settings.
	 * 
	 * @param uuids The set of player UUIDs on the server.
	 */
	public abstract void updatePlayerList(Set<UUID> uuids);
	
	/**
	 * Gets the effective lock coordinator for the network.
	 * 
	 * @see LockCoordinator
	 * @see PersistentPlayerIdentity
	 * 
	 * @return The network lock coordinator to use for locking on resources.
	 */
	public abstract LockCoordinator getLockCoordinator();
	
}
