package io.neocore.player.permission;

public interface PermissionCollection {
	
	public void setPermissionState(String permission, boolean state);
	public void unsetPermissionState(String permission);
	
}
