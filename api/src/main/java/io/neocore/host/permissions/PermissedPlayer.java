package io.neocore.host.permissions;

import io.neocore.player.PlayerIdentity;

public interface PermissedPlayer extends PlayerIdentity {
	
	public boolean isOp();
	public boolean hasPermission(String perm);
	
}
