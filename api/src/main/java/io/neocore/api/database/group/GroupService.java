package io.neocore.api.database.group;

import java.util.List;

import io.neocore.api.database.DatabaseServiceProvider;
import io.neocore.api.player.group.Group;

public interface GroupService extends DatabaseServiceProvider {
	
	/**
	 * Reload the group cache from the database.
	 * 
	 * @return A new list of all groups in the database.
	 */
	public List<Group> loadGroups();
	
	
	
}
