package io.neocore.player.permission;

import io.neocore.player.NeoPlayer;

public abstract class Permission {
	
	private final String name;
	
	public Permission(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public abstract boolean getState(NeoPlayer player);
	
}
