package io.neocore.api.host.permissions;

import java.util.Map;
import java.util.Set;

public interface PermissionCollection {

	/**
	 * Checks to see if this collection has the specified permission set.
	 * 
	 * @param perm
	 *            The permission.
	 * @return If the permission is set or not.
	 */
	public boolean isPermissionSet(String perm);

	/**
	 * Sets the value for the specified permission to the value specified.
	 * 
	 * @param perm
	 *            The permission to set.
	 * @param value
	 *            The state to set it to.
	 */
	public void setPermission(String perm, boolean value);

	/**
	 * Removes the setting for the given permission.
	 * 
	 * @param perm
	 *            The state to set the permission to.
	 */
	public void unsetPermission(String perm);

	/**
	 * Adds the tag onto the permission collection.
	 * 
	 * @param tag
	 *            The tag to add.
	 */
	public void addTag(String tag);

	/**
	 * Checks to see if the tag is on the permission collection.
	 * 
	 * @param tag
	 *            The tag to check for.
	 * @return If the collection has the tag.
	 */
	public boolean hasTag(String tag);

	/**
	 * @return A copy of the set of tags on the collection.
	 */
	public Set<String> getTags();

	/**
	 * @return A list of permissions applied through this collection.
	 */
	public Map<String, Boolean> getPermissionsApplied();

}
