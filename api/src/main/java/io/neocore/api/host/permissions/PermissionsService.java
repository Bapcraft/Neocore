package io.neocore.api.host.permissions;

import java.util.UUID;

import io.neocore.api.host.HostServiceProvider;

public interface PermissionsService extends HostServiceProvider {
	
	public PermissedPlayer getPlayer(UUID uuid);
	
}
