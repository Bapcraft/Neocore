package io.neocore.api.host.permissions;

import io.neocore.api.host.HostServiceProvider;
import io.neocore.api.player.IdentityProvider;
import io.neocore.api.player.PlayerIdentity;

public interface PermissionsService extends HostServiceProvider, IdentityProvider<PermissedPlayer> {
	
	// TODO Add a permission API.
	
	@Override
	default Class<? extends PlayerIdentity> getIdentityClass() {
		return PermissedPlayer.class;
	}
	
}
