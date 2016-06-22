package io.neocore.host.permissions;

import io.neocore.player.PlayerIdentity;
import io.neocore.player.permission.PermissionCollection;

public interface PermissedPlayer extends PlayerIdentity {
	
	public boolean isOp();
	public boolean hasPermission(String perm);
	
	public void addPermCollection(PermissionCollection collection);
	public void removePermCollection(PermissionCollection collection);
	public void isPermSet(String permission);
	
	public void recalculatePermissions();
	
}
