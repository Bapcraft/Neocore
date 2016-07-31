package io.neocore.api.host.permissions;

import io.neocore.api.player.PlayerIdentity;
import io.neocore.api.player.permission.DynamicPermissionCollection;

public interface PermissedPlayer extends PlayerIdentity {
	
	public boolean isOp();
	public boolean hasPermission(String perm);
	
	public DynamicPermissionCollection addPermCollection();
	public void removePermCollection(DynamicPermissionCollection collection);
	public void isPermSet(String permission);
	
	public void recalculatePermissions();
	
}
