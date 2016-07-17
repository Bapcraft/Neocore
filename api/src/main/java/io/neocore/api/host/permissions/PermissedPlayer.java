package io.neocore.api.host.permissions;

import io.neocore.api.player.PlayerIdentity;
import io.neocore.api.player.permission.PermissionCollection;

public interface PermissedPlayer extends PlayerIdentity {
	
	public boolean isOp();
	public boolean hasPermission(String perm);
	
	public PermissionCollection addPermCollection();
	public void removePermCollection(PermissionCollection collection);
	public void isPermSet(String permission);
	
	public void recalculatePermissions();
	
}
