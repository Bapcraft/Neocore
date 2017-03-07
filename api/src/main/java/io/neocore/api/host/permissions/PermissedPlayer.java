package io.neocore.api.host.permissions;

import java.util.List;

import io.neocore.api.host.ConnectingPlayer;

/**
 * Represents a player that can have permissions attached to them.
 * 
 * @author treyzania
 */
public interface PermissedPlayer extends ConnectingPlayer {

	/**
	 * @return <code>true</code> if the player is an "operator",
	 *         <code>false</code> otherwise.
	 */
	public boolean isOp();

	/**
	 * Checks to see if the player has the permission specified.
	 * 
	 * @param perm
	 *            The permission to check for.
	 * @return The state of the permission.
	 */
	public boolean hasPermission(String perm);

	/**
	 * Checks to see if there is a definition for the specified permission on
	 * the player.
	 * 
	 * @param permission
	 *            The permission to check.
	 * @return TODO
	 */
	public boolean isPermSet(String permission);

	/**
	 * Gets a new, empty permission collection for the player.
	 * 
	 * @return The new collection.
	 */
	public PermissionCollection createCollection();

	/**
	 * Removes the permission collection from the player.
	 * 
	 * @param col
	 *            The collection to remove.
	 */
	public void removeCollection(PermissionCollection col);

	/**
	 * Returns a list of all of the permission collections that have been
	 * created by this object in the cache. Changes to the returned list should
	 * not effect the actual collections allocated.
	 * 
	 * @return A list of cached collections.
	 */
	public List<PermissionCollection> getCollections();

	/**
	 * Optionally performs an update of the underlying player permissions in the
	 * event they do not normally happen immediately.
	 */
	public default void applyChanges() {

	}

}
