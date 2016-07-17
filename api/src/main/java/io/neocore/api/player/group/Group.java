package io.neocore.api.player.group;

import io.neocore.api.player.permission.Permission;

public interface Group {
	
	/**
	 * @return The simple name of this group.
	 */
	public String getName();
	
	/**
	 * @return The group that this group inherits from.
	 */
	public Group getParent();
	
	/**
	 * @return An array of all the Flair that this Group provides to the player.
	 */
	public Flair[] getFlair();
	
	/**
	 * Get information about what state the permission is in the group's configuration.
	 * 
	 * @param permission The permission to be checked.
	 * @return The permission defined in the database, <code>null</code> if undefined.
	 */
	public Permission getStatedPermission(String permission);
	
}
