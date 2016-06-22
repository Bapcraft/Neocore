package io.neocore.database.player;

import io.neocore.player.extension.PlayerExtension;
import io.neocore.player.group.Group;
import io.neocore.player.group.GroupMembership;

public interface DatabasePlayer {
	
	/**
	 * @return An array of all the groups this player has.
	 */
	public GroupMembership[] getGroupMemberships();
	
	/**
	 * Adds the group specified to the player in the database, and commits the change.
	 * 
	 * @param group The group to be added.
	 * @return <code>true</code> if the group was newly added, <code>false</code> if it was already there.
	 */
	public boolean addGroup(Group group);
	
	/**
	 * Removes the group specified from the player in the databse, and commits the change.
	 * 
	 * @param group The group to be removed.
	 * @return <code>true</code> if the group was removed, <code>false</code> if it wasn't there in the first place.
	 */
	public boolean removeGroup(Group group);
	
	/**
	 * @return An array of all of the extensions the player currently has.
	 */
	public PlayerExtension[] getExtensions();
	
}
