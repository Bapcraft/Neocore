package io.neocore.api.player.permission;

public interface PermissionCollection {
	
	public void setPermissionState(String permission, boolean state);
	public void unsetPermissionState(String permission);
	
}
