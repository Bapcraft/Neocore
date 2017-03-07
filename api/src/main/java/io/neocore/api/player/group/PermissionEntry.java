package io.neocore.api.player.group;

import io.neocore.api.host.Context;

public interface PermissionEntry {

	/**
	 * Gets the context that the permission entry is active in, or null if
	 * global.
	 * 
	 * @return The context to be active in.
	 */
	public Context getContext();

	/**
	 * @return <code>true</code> if this permission entry is active globally.
	 */
	public default boolean isGlobal() {
		return this.getContext() == null;
	}

	/**
	 * @return The path of the permission node(s) that the entry applies to.
	 */
	public String getPermissionNode();

	/**
	 * @return If the permission is set to anything.
	 */
	public boolean isSet();

	/**
	 * @return The state that the permission node gets set to.
	 */
	public boolean getState();

}
