package io.neocore.player.permission;

import io.neocore.player.NeoPlayer;

public class ExplicitPermission extends Permission {

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
