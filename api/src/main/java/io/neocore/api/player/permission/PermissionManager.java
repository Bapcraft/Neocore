package io.neocore.api.player.permission;

import java.util.List;

import io.neocore.api.player.NeoPlayer;
import io.neocore.api.player.group.Group;

public interface PermissionManager {

	public Group createGroup(String name);

	public boolean deleteGroup(Group group);

	public default boolean deleteGroup(String name) {
		return this.deleteGroup(this.getGroup(name));
	}

	public Group getGroup(String name);

	public List<Group> getGroups();

	public void assignPermissions(NeoPlayer player);

	public void repopulatePermissions();

}
