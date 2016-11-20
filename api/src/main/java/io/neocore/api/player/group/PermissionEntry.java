package io.neocore.api.player.group;

public final class PermissionEntry {
	
	public final String permission;
	public final boolean state;
	
	public PermissionEntry(String perm, boolean state) {
		
		this.permission = perm;
		this.state = state;
		
	}
	
}
