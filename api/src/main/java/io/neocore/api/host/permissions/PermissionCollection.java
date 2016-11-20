package io.neocore.api.host.permissions;

public interface PermissionCollection {
	
	/**
	 * Checks to see if this collection has the specified permission set.
	 * 
	 * @param perm The permission.
	 * @return If the permission is set or not.
	 */
	public boolean isPermissionSet(String perm);
	
	/**
	 * Sets the value for the specified permission to the value specified.
	 * 
	 * @param perm The permission to set.
	 * @param value The state to set it to.
	 */
	public void setPermission(String perm, boolean value);
	
	/**
	 * Removes the setting for the given permission.
	 * 
	 * @param perm The state to set the permission to.
	 */
	public void unsetPermission(String perm);
	
}
