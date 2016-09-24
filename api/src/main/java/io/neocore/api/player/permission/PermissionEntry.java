package io.neocore.api.player.permission;

public abstract class PermissionEntry {
	
	private String permissionName;
	
	public PermissionEntry(String perm) {
		this.permissionName = perm;
	}
	
	public String getName() {
		return this.permissionName;
	}

	@Override
	public int hashCode() {
		return this.permissionName.hashCode();
	}
	
}
