package io.neocore.api.player.permission;

import io.neocore.api.player.NeoPlayer;

/**
 * A dynamic permission that can be defined in relation to the states of other
 * permissions.
 * 
 * @author treyzania
 */
public abstract class DynPerm {
	
	private final String name;
	
	public DynPerm(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	/**
	 * Gets the state of this permission for the given player.
	 * 
	 * @param player The player.
	 * @return The result.
	 */
	public abstract boolean getState(NeoPlayer player);
	
}
