package io.neocore.api.player.permission;

import io.neocore.api.player.NeoPlayer;

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
