package io.neocore.api.database.group;

import io.neocore.api.database.DatabaseServiceProvider;
import io.neocore.api.player.group.Group;


public interface GroupProvider extends DatabaseServiceProvider {
	
	/**
	 * @return A list of all of the groups in the database.
	 */
	public Group[] getGroups();
	
}
