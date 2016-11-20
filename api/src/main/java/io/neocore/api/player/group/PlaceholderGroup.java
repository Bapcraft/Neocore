package io.neocore.api.player.group;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A placeholder group used when an actual Group reference cannot be provided.
 * 
 * @author treyzania
 */
public class PlaceholderGroup implements Group {
	
	private String name;
	private int level, priority;
	
	public PlaceholderGroup(String name, int level, int priority) {
		
		this.name = name;
		this.level = level;
		this.priority = priority;
		
	}
	
	public PlaceholderGroup(String name) {
		this(name, 0, 0);
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
	public List<Flair> getFlairs() {
		return Collections.emptyList();
	}
	
	@Override
	public int addFlair(Flair flair) {
		return -1; // I wanted to do 42, but someone would find a way to break something with that.
	}

	@Override
	public void deleteFlair(int index) {
		
	}

	@Override
	public Map<String, Set<PermissionEntry>> getPermissions() {
		return null;
	}

	@Override
	public void putPermission(String context, PermissionEntry permission) {
		// No.
	}
	
	@Override
	public int getRestrictionLevel() {
		return this.level;
	}
	
	@Override
	public int getPriority() {
		return this.priority;
	}
	
}
