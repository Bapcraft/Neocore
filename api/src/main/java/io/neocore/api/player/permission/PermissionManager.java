package io.neocore.api.player.permission;

import java.util.List;

import io.neocore.api.player.group.Group;

public interface PermissionManager {
	
	/**
	 * Gets a list of all of the loaded groups.
	 * @return
	 */
	public List<Group> getGroups();
	
	/**
	 * Finds the group with the specified name if it's loaded.
	 * 
	 * @param name The name of the group.
	 * @return The group instance, or <code>null</code> if undefined.
	 */
	public default Group forName(String name) {
		
		for (Group g : this.getGroups()) {
			if (g.getName().equals(name)) return g;
		}
		
		return null;
		
	}
	
}
