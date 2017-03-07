package io.neocore.api.database.group;

import java.util.List;

import io.neocore.api.database.DatabaseServiceProvider;
import io.neocore.api.player.group.Group;

public interface GroupService extends DatabaseServiceProvider {

	/**
	 * Reload the group cache from the database.
	 * 
	 * @return The groups in the cache.
	 */
	public List<Group> loadGroups();

	/**
	 * Creates a new group and adds it to the cache and database.
	 * 
	 * @param name
	 *            The system name of the group.
	 * @return The newly created group.
	 */
	public Group createGroup(String name);

	/**
	 * Removes the entry for the group in the database.
	 * 
	 * @param group
	 *            The group to be deleted.
	 * @return If the group was deleted successfully.
	 */
	public boolean deleteGroup(Group group);

}
