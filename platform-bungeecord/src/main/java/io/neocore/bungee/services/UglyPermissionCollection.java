package io.neocore.bungee.services;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import io.neocore.api.host.permissions.PermissionCollection;

public class UglyPermissionCollection implements PermissionCollection {
	
	private BungeePermPlayer wrapper;
	
	private Set<String> tags = new HashSet<>();
	private Map<String, Boolean> perms = new HashMap<>();
	
	public UglyPermissionCollection(BungeePermPlayer wrapper) {
		this.wrapper = wrapper;
	}
	
	@Override
	public boolean isPermissionSet(String perm) {
		return this.wrapper.getPlayerOrThrow().hasPermission(perm);
	}

	@Override
	public void setPermission(String perm, boolean value) {
		
		this.perms.put(perm, value);
		this.wrapper.getPlayerOrThrow().setPermission(perm, value);
		
	}

	@Override
	public void unsetPermission(String perm) {
		
		this.setPermission(perm, false);
		this.perms.remove(perm); // Do it afterwards so it's obvious it was *unset* instead of set to false.
		
	}

	@Override
	public void addTag(String tag) {
		this.tags.add(tag);
	}

	@Override
	public boolean hasTag(String tag) {
		return this.tags.contains(tag);
	}

	@Override
	public Set<String> getTags() {
		return this.tags;
	}

	@Override
	public Map<String, Boolean> getPermissionsApplied() {
		return new HashMap<>(this.perms);
	}

}
