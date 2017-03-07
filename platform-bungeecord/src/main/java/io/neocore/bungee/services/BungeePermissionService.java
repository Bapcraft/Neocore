package io.neocore.bungee.services;

import java.io.IOException;
import java.util.UUID;

import io.neocore.api.host.permissions.PermissedPlayer;
import io.neocore.api.host.permissions.PermissionsService;

public class BungeePermissionService implements PermissionsService {

	@Override
	public PermissedPlayer load(UUID uuid) throws IOException {
		return new BungeePermPlayer(uuid);
	}

}
