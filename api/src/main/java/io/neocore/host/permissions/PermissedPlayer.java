package io.neocore.host.permissions;

public interface PermissedPlayer {
	
	public boolean isOp();
	public boolean hasPermission(String perm);
	
}
