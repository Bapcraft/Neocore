package io.neocore.api.host.permissions;

import java.util.UUID;

import io.neocore.api.host.HostServiceProvider;

public interface PermissionsProvider extends HostServiceProvider {
	
	public PermissedPlayer getPlayer(UUID uuid);
	
}
