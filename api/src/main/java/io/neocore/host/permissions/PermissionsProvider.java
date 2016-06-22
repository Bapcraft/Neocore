package io.neocore.host.permissions;

import java.util.UUID;

import io.neocore.host.HostServiceProvider;

public interface PermissionsProvider extends HostServiceProvider {
	
	public PermissedPlayer getPlayer(UUID uuid);
	
}
