package io.neocore.database.group;

import io.neocore.database.DatabaseServiceProvider;
import io.neocore.player.group.Group;


public interface GroupProvider extends DatabaseServiceProvider {
	
	/**
	 * @return A list of all of the groups in the database.
	 */
	public Group[] getGroups();
	
}
