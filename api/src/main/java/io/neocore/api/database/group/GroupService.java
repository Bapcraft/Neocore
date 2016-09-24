package io.neocore.api.database.group;

import java.util.List;

import io.neocore.api.database.DatabaseServiceProvider;
import io.neocore.api.player.group.Group;

public interface GroupService extends DatabaseServiceProvider {
	
	/**
	 * @return A list of all of the groups in the database.
	 */
	public List<Group> getGroups();
	
	/**
	 * Gets the group with the specified name.
	 * 
	 * @param name The group name.
	 * @return The group, or <code>null</code> if undefined.
	 */
	public default Group getGroup(String name) {
		
		for (Group g : this.getGroups()) {
			if (g.getName().equals(name)) return g;
		}
		
		return null;
		
	}
	
}
