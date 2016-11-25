package io.neocore.api.player.group;

import java.util.ArrayList;
import java.util.List;

import io.neocore.api.database.Persistent;
import io.neocore.api.host.Context;

public interface Group extends Persistent, Comparable<Group> {
	
	/**
	 * Sets the name of the group.
	 * 
	 * @param name The new name.
	 */
	public void setName(String name);
	
	/**
	 * @return The simple name of this group.
	 */
	public String getName();
	
	/**
	 * Sets the name of the group that will be shown to end-users.
	 * 
	 * @param displayName The display name.
	 */
	public void setDisplayName(String displayName);
	
	/**
	 * @return The name of the group to be displayed to end-users.
	 */
	public String getDisplayName();
	
	/**
	 * Sets the parent group for this group.  Returns null if it has no parent.
	 * The group will inherit all the permissions of this group.
	 * 
	 * @param parent The group to be the new parent.
	 */
	public void setParent(Group parent);
	
	/**
	 * @return The group that this group inherits from.
	 */
	public Group getParent();
	
	/**
	 * Adds the specified flair to the list.
	 * 
	 * @param flair The flair to add.
	 */
	public void addFlair(Flair flair);
	
	/**
	 * Removed the matching flair.  Matching is based on the prefix and suffix
	 * being the same.
	 * 
	 * @param flair The flair to remove.
	 * @return If a flair was removed or not.
	 */
	public boolean removeFlair(Flair flair);

	/**
	 * @return A list of all the Flairs that this Group provides to the player.
	 */
	public List<Flair> getFlairs();
	
	/**
	 * Sets the permission stated in the given context to the given state.
	 * 
	 * @param context The context to set it in, or <code>null</code> if global.
	 * @param node The permission node(s) to apply to.
	 * @param state The state to set it to.
	 */
	public void setPermission(Context context, String node, boolean state);
	
	/**
	 * Removes 
	 * 
	 * @param context
	 * @param node
	 */
	public void unsetPermission(Context context, String node);

	/**
	 * @return A map of all of the permissions brought by this group, by context name.
	 */
	public List<PermissionEntry> getPermissions();
	
	/**
	 * Sets the priority of the group.  This is used in calculating the
	 * effective permissions after resolving inheritance chains.
	 * 
	 * @param priority The priority.
	 */
	public void setPriority(int priority);
	
	/**
	 * Gets the priority of this group.  Higher priorities are applied later,
	 * so will override lower ones.
	 * 
	 * @return The priority level.
	 */
	public int getPriority();
	
	/**
	 * Sets the restriction level of this group.  The higher the restriction
	 * level, the higher the restriction level server operators need to modify
	 * the group and manipulate memberships of this group.
	 * 
	 * @param restriction The restriction level of this group.
	 */
	public void setRestrictionLevel(int restriction);
	
	/**
	 * Returns the restriction level of this Group object.  The higher the
	 * restriction level, the higher the restriction level server operators
	 * need to modify the group and manipulate memberships of this group.
	 * 
	 * @return The restriction level of this group.
	 */
	public int getRestrictionLevel();
	
	public default List<Group> getAncestors() {
		
		Group parent = this.getParent();
		
		if (parent != null || parent == this) {
			
			List<Group> parentGroups = parent.getAncestors();
			parentGroups.add(0, parent); // Make our parent the immediate ancestor.
			return parentGroups;
			
		} else {
			return new ArrayList<>();
		}
		
	}
	
	@Override
	public default int compareTo(Group o) {
		return this.getPriority() - o.getPriority();
	}
	
}
