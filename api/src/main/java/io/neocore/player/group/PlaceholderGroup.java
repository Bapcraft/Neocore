package io.neocore.player.group;

import io.neocore.player.permission.Permission;

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
	public Flair[] getFlair() {
		return new Flair[0];
	}

	@Override
	public Permission getStatedPermission(String permission) {
		return null;
	}

}
