package io.neocore.api.player.permission;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DynamicPermissionCollection {
	
	private List<Permission> permissions;
	
	public DynamicPermissionCollection() {
		this.permissions = new ArrayList<>();
	}
	
	public void addDynamicPermission(Permission perm) {
		
		if (!this.isSet(perm.getName())) {
			this.permissions.add(perm);
		} else {
			throw new IllegalStateException("Permission of this name is already applied on Collection!");
		}
		
	}
	
	public int removeDynamicPermissionForName(String name) {
		
		int count = 0;
		
		Iterator<Permission> perms = this.permissions.iterator();
		while (perms.hasNext()) {
			
			if (perms.next().getName().equalsIgnoreCase(name)) {
				
				perms.remove();
				count++;
				
			}
			
		}
		
		return count;
		
	}
	
	public boolean isSet(String name) {
		
		for (Permission perm : this.permissions) {
			if (perm.getName().equalsIgnoreCase(name)) return true;
		}
		
		return false;
		
	}
	
	public Permission getPerm(String name) {
		
		for (Permission perm : this.permissions) {
			if (perm.getName().equalsIgnoreCase(name)) return perm;
		}
		
		return null;
		
	}
	
}
