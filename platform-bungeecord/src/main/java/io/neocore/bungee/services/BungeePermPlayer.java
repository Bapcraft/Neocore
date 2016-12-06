package io.neocore.bungee.services;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import io.neocore.api.host.permissions.PermissedPlayer;
import io.neocore.api.host.permissions.PermissionCollection;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class BungeePermPlayer implements PermissedPlayer {
	
	private ProxiedPlayer player;
	private UglyPermissionCollection perms;
	
	public BungeePermPlayer(ProxiedPlayer pp) {
		
		this.player = pp;
		this.perms = new UglyPermissionCollection(pp);
		
	}
	
	@Override
	public UUID getUniqueId() {
		return this.player.getUniqueId();
	}
	
	@Override
	public boolean isOp() {
		return false;
	}
	
	@Override
	public boolean hasPermission(String perm) {
		return this.player.hasPermission(perm);
	}
	
	@Override
	public boolean isPermSet(String permission) {
		return this.hasPermission(permission);
	}
	
	@Override
	public PermissionCollection createCollection() {
		return this.perms;
	}
	
	@Override
	public void removeCollection(PermissionCollection col) {
		
		/*
		 * TODO Make these permission collections keep track of the permissions
		 *      they are responsible for and recalculate them a la Bukkit when
		 *      we make a change to things for BungeeCord. 
		 */
		
	}
	
	@Override
	public List<PermissionCollection> getCollections() {
		return Arrays.asList(this.perms);
	}
	
}
