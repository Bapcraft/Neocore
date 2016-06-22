package io.neocore.host.permissions;

import java.util.UUID;

import io.neocore.host.HostServiceProvider;
import io.neocore.player.permission.PermissionCollection;

public interface PermissionsProvider extends HostServiceProvider {
	
	public PermissedPlayer getPlayer(UUID uuid);
	
	public PermissionCollection createNewCollection();
	
}
