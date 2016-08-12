package io.neocore.api.database.player;

import io.neocore.api.NeocoreAPI;
import io.neocore.api.database.ban.BanList;
import io.neocore.api.database.ban.BanService;
import io.neocore.api.player.PlayerIdentity;
import io.neocore.api.player.extension.Extension;
import io.neocore.api.player.group.Group;
import io.neocore.api.player.group.GroupMembership;

public interface DatabasePlayer extends PlayerIdentity {
	
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
	public Extension[] getExtensions();
	
	/**
	 * Saves the data to the backend database.
	 */
	public void flush();
	
	/**
	 * Saves the data to the backend, and invalidates any caches.  The reference to this player should be made invalid.
	 */
	public void unload();
	
	public default BanList getBans() {
		return NeocoreAPI.getAgent().getServiceManager().getService(BanService.class).getBans(this.getUniqueId());
	}
	
}
