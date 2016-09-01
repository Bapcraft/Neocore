package io.neocore.api.player.permission;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A collection of permissions whose state is defined by that of other permissions.
 * 
 * @author treyzania
 */
public class DynamicPermissionCollection {
	
	private List<DynPerm> permissions;
	
	public DynamicPermissionCollection() {
		this.permissions = new ArrayList<>();
	}
	
	/**
	 * Adds a permission into the collection.
	 * 
	 * @param perm The permission.
	 */
	public void addDynamicPermission(DynPerm perm) {
		
		if (!this.isSet(perm.getName())) {
			this.permissions.add(perm);
		} else {
			throw new IllegalStateException("Permission of this name is already applied on Collection!");
		}
		
	}
	
	/**
	 * Removed a permission from the collection.
	 * 
	 * @param name The permission node.
	 * @return How many it ended up removing.
	 */
	public int removeDynamicPermissionForName(String name) {
		
		int count = 0;
		
		Iterator<DynPerm> perms = this.permissions.iterator();
		while (perms.hasNext()) {
			
			if (perms.next().getName().equalsIgnoreCase(name)) {
				
				perms.remove();
				count++;
				
			}
			
		}
		
		return count;
		
	}
	
	/**
	 * Checks to see if the permission has a definition in the collection.
	 * 
	 * @param name The name of the permission.
	 * @return If it is set or not.
	 */
	public boolean isSet(String name) {
		
		for (DynPerm perm : this.permissions) {
			if (perm.getName().equalsIgnoreCase(name)) return true;
		}
		
		return false;
		
	}
	
	/**
	 * Gets the permisison by name, exactly.
	 * 
	 * @param name The permission node.
	 * @return The permission itself.
	 */
	public DynPerm getPerm(String name) {
		
		for (DynPerm perm : this.permissions) {
			if (perm.getName().equalsIgnoreCase(name)) return perm;
		}
		
		return null;
		
	}
	
}
