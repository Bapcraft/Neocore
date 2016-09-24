package io.neocore.api.player.permission;

public class SimplePermissionEntry extends PermissionEntry {
	
	public final boolean state;
	
	public SimplePermissionEntry(String perm, boolean value) {
		
		super(perm);
		
		this.state = value;
		
	}
	
}
