package io.neocore.api.player.group;

import io.neocore.api.player.permission.Permission;

/**
 * A placeholder group used when an actual Group reference cannot be provided.
 * 
 * @author treyzania
 */
public class PlaceholderGroup implements Group {
	
	private String name;
	
	public PlaceholderGroup(String name) {
		this.name = name;
	}
	
	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public Group getParent() {
		return new PlaceholderGroup(this.getName() + "_parent");
	}

	@Override
	public AbstractFlair[] getFlairs() {
		return new AbstractFlair[0];
	}

	@Override
	public Permission getStatedPermission(String permission) {
		return null;
	}

}
