package io.neocore.api.host.permissions;

import io.neocore.api.player.PlayerIdentity;
import io.neocore.api.player.permission.DynamicPermissionCollection;

/**
 * Represents a player that can have permissions attached to them.
 * 
 * @author treyzania
 */
public interface PermissedPlayer extends PlayerIdentity {
	
	/**
	 * @return <code>true</code> if the player is an "operator", <code>false</code> otherwise.
	 */
	public boolean isOp();
	
	/**
	 * Checks to see if the player has the permission specified.
	 * 
	 * @param perm The permission to check for.
	 * @return The state of the permission.
	 */
	public boolean hasPermission(String perm);
	
	/**
	 * Adds a new dynamic permission collection to the player and returns it.
	 * 
	 * @return The new dynamic permission collection.
	 */
	public DynamicPermissionCollection addPermCollection();
	
	/**
	 * Removes the specified permission collection from the player.
	 * 
	 * @param collection The collection to remove.
	 */
	public void removePermCollection(DynamicPermissionCollection collection);
	
	/**
	 * Checks to see if there is a definition for the specified permission on the player.
	 * 
	 * @param permission The permission to check.
	 */
	public void isPermSet(String permission);
	
	/**
	 * Recalculates the static permission states, which may change the
	 * resultant states of dynamic permission collections.
	 */
	public void recalculatePermissions();
	
}
