package io.neocore.api.player.permission;

import io.neocore.api.player.NeoPlayer;

/**
 * A dynamic permission with explicit values, behaving much like the classic
 * static permissions.
 * 
 * @author treyzania
 */
public class ExplicitPermission extends DynPerm {

	private final boolean value;
	
	public ExplicitPermission(String name, boolean value) {
		
		super(name);
		
		this.value = value;
		
	}

	@Override
	public boolean getState(NeoPlayer player) {
		return this.value;
	}
	
}
