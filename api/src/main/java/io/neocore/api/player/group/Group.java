package io.neocore.api.player.group;

import java.util.List;
import java.util.Map;
import java.util.Set;

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
	public List<Flair> getFlairs();
	
	/**
	 * Adds the specified flair to the list.
	 * 
	 * @param flair The flair to add.
	 * @return The index of the newly-added flair.
	 */
	public int addFlair(Flair flair);
	
	/**
	 * Deletes a flair from the records at the specified index.
	 * 
	 * @param index The index to delete.
	 */
	public void deleteFlair(int index);
	
	/**
	 * @return A map of all of the permissions brought by this group, by context name.
	 */
	public Map<String, Set<PermissionEntry>> getPermissions();
	
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
	
}
