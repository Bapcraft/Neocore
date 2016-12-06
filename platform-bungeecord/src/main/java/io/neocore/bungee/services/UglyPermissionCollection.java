package io.neocore.bungee.services;

import java.util.HashSet;
import java.util.Set;

import io.neocore.api.host.permissions.PermissionCollection;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class UglyPermissionCollection implements PermissionCollection {
	
	private ProxiedPlayer player;
	
	private Set<String> tags = new HashSet<>();
	
	public UglyPermissionCollection(ProxiedPlayer pp) {
		this.player = pp;
	}
	
	@Override
	public boolean isPermissionSet(String perm) {
		return this.player.hasPermission(perm);
	}

	@Override
	public void setPermission(String perm, boolean value) {
		this.player.setPermission(perm, value);
	}

	@Override
	public void unsetPermission(String perm) {
		this.setPermission(perm, false);
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
		// TODO Auto-generated method stub
		return null;
	}

}
