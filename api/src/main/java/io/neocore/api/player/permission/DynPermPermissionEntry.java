package io.neocore.api.player.permission;

public class DynPermPermissionEntry extends PermissionEntry {
	
	public final DynPerm permission;
	
	public DynPermPermissionEntry(DynPerm perm) {
		
		super(perm.getName());
		
		this.permission = perm;
		
	}

}
