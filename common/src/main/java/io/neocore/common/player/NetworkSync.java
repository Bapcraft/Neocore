package io.neocore.common.player;

import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

import io.neocore.api.database.PersistentPlayerIdentity;

public abstract class NetworkSync {
	
	/**
	 * Potentially announces an update in the subscription state for the given
	 * unique ID.  If the state is essentially unchanged, then nothing is
	 * announced.  This may flip flop rapidly in some cases, so plan to handle
	 * traffic congestion accordingly.
	 * 
	 * @param uuid The UUID of the player
	 * @param state The "new" state
	 */
	public abstract void updateSubscriptionState(UUID uuid, boolean state);
	
	/**
	 * Announces the player list on the server, effectively overriding all
	 * previous state settings.
	 * 
	 * @param uuids The set of player UUIDs on the server
	 */
	public abstract void updatePlayerList(Set<UUID> uuids);
	
	/**
	 * Announces an invalidation of the UUID to subscribed clients on the
	 * server.
	 * 
	 * @param uuid The UUID of the invalidated player
	 */
	public abstract void announceInvalidation(UUID uuid);
	
	/**
	 * Provides a callback to invoke and provide a UUID for when an
	 * invalidation is received from across the network, regardless of whether
	 * we are supposed to receive it or not.  So if we don't have a player with
	 * this UUID on our server then we still should give it to the consumer.
	 * If <code>null</code> is passed into this method then nothing should be
	 * done when invalidation is received.
	 * 
	 * @param callback The invalidation callback
	 */
	public abstract void setInvalidationCallback(Consumer<UUID> callback);
	
	/**
	 * Gets the effective lock coordinator for the network.
	 * 
	 * @see LockCoordinator
	 * @see PersistentPlayerIdentity
	 * 
	 * @return The network lock coordinator to use for locking on resources
	 */
	public abstract LockCoordinator getLockCoordinator();
	
}
