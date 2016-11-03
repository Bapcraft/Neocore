package io.neocore.common.player;

import java.util.UUID;

/**
 * Coordinates the locks for the specified identity type across the network.
 * 
 * @author treyzania
 */
public abstract class LockCoordinator {
	
	public LockCoordinator() {
		
	}

	/**
	 * Creates a new lock for the identity.
	 * 
	 * @param uuid The UUID of the identity.
	 * @return The lock ID, currently means nothing.
	 */
	public abstract int lock(UUID uuid);
	
	/**
	 * Releases the lock for the identity.
	 * 
	 * @param uuid The UUID of the identity.
	 */
	public abstract void unlock(UUID uuid);
	
	/**
	 * Checks to see if there is a lock on the identity for the UUID.
	 * 
	 * @param uuid The identity to check for.
	 * @return If the identity is locked.
	 */
	public abstract boolean isLocked(UUID uuid);
	
	/**
	 * Blocks until the identity is unlocked, or until it times out.
	 * 
	 * @param uuid The UUID of the identity.
	 * @param timeout Timeout, in milliseconds.
	 */
	public abstract void blockUntilUnlocked(UUID uuid, long timeout);
	
	/**
	 * Blocks until the identity is unlocked, or until it times out in a few
	 * billion years.
	 * 
	 * @param uuid The UUID of the identity.
	 */
	public void blockUntilUnlocked(UUID uuid) {
		this.blockUntilUnlocked(uuid, Long.MAX_VALUE);
	}
	
}
