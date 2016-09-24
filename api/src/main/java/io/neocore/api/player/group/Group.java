package io.neocore.api.player.group;

import java.util.List;
import java.util.Map;
import java.util.Set;

import io.neocore.api.player.permission.PermissionEntry;

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
	 * @return A list of all the Flairs that this Group provides to the player.
	 */
	public List<AbstractFlair> getFlairs();
	
	/**
	 * Adds the specified flair to the list.
	 * 
	 * @param flair The flair to add.
	 * @return The index of the newly-added flair.
	 */
	public int addFlair(AbstractFlair flair);
	
	/**
	 * Deletes a flair from the records at the specified index.
	 * 
	 * @param index The index to delete.
	 */
	public void deleteFlair(int index);
	
	/**
	 * @return A map of all of the permissions brought by this group.
	 */
	public Map<String, Set<PermissionEntry>> getPermissions();
	
	/**
	 * Get information about what state the permission is in the group's configuration.
	 * 
	 * @param contextName the name of the context in which to check.
	 * @param permission The permission to be checked.
	 * @return The permission defined in the database, <code>null</code> if undefined.
	 */
	public default PermissionEntry getStatedPermission(String contextName, String permission) {
		
		Set<PermissionEntry> perms = this.getPermissions().get(contextName);
		if (perms == null) return null;
		
		for (PermissionEntry entry : perms) {
			if (entry.getName().equals(permission)) return entry;
		}
		
		return null;
		
	}
	
	/**
	 * Puts the permission into the records, ignoring what might have been there before.
	 * 
	 * @param context The name of the context to put the permission into.
	 * @param permission The permission.
	 */
	public void putPermission(String context, PermissionEntry permission);
	
	/**
	 * Gets the priority of this group.  Higher priorities are applied later,
	 * so will override lower ones.
	 * 
	 * @return The priority level.
	 */
	public int getPriority();
	
	/**
	 * Returns the restriction level of this Group object.  The higher the
	 * restriction level, the higher the restriction level server operators
	 * need to modify the group and manipulate memberships of this group.
	 * 
	 * @return The restriction level of this group.
	 */
	public int getRestrictionLevel();
	
	/**
	 * Flushed the current information into the database.
	 */
	public void flush();
	
}
