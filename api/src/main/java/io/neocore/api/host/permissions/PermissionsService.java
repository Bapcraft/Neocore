package io.neocore.api.host.permissions;

import io.neocore.api.host.HostServiceProvider;
import io.neocore.api.player.IdentityProvider;

public interface PermissionsService extends HostServiceProvider, IdentityProvider<PermissedPlayer> {
	
}
