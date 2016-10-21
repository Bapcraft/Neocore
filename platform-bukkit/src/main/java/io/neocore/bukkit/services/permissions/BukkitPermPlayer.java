package io.neocore.bukkit.services.permissions;

import java.util.UUID;

import org.bukkit.entity.Player;

import io.neocore.api.host.permissions.PermissedPlayer;
import io.neocore.api.player.permission.DynamicPermissionCollection;
import io.neocore.bukkit.PermissionInjector;
import io.neocore.bukkit.permissions.DynamicPermissibleBase;
import io.neocore.common.player.CommonPlayerManager;

public class BukkitPermPlayer implements PermissedPlayer {
	
	private Player player;
	private DynamicPermissibleBase perms;
	
	public BukkitPermPlayer(PermissionInjector injector, CommonPlayerManager cpm, Player p) {
		
		this.player = p;
		this.perms = injector.injectPermissions(p.getUniqueId());
		
		this.perms.setPlayer(() -> {
			return cpm.getPlayer(this.player.getUniqueId());
		});
		
	}
	
	@Override
	public UUID getUniqueId() {
		return this.player.getUniqueId();
	}

	@Override
	public boolean isOp() {
		return this.player.isOp();
	}

	@Override
	public boolean hasPermission(String perm) {
		return this.player.hasPermission(perm);
	}

	@Override
	public DynamicPermissionCollection addPermCollection() {
		return this.perms.getNewDynamicPermissionCollection(); // Gets a new collection from the injector's result.
	}

	@Override
	public void removePermCollection(DynamicPermissionCollection collection) {
		// TODO Auto-generated method stub
	}

	@Override
	public void isPermSet(String permission) {
		this.player.isPermissionSet(permission);
	}

	@Override
	public void recalculatePermissions() {
		this.player.recalculatePermissions();
	}

}
