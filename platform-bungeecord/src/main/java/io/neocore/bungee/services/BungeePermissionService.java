package io.neocore.bungee.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.neocore.api.host.permissions.PermissedPlayer;
import io.neocore.api.host.permissions.PermissionsService;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class BungeePermissionService implements PermissionsService {
	
	private List<BungeePermPlayer> cache = new ArrayList<>();
	
	public BungeePermissionService() {
		
	}
	
	@Override
	public PermissedPlayer load(UUID uuid) {
		
		BungeePermPlayer bpp = this.findPlayer(uuid);
		if (bpp == null) bpp = this.initPlayer(uuid);
		return bpp;
		
	}
	
	private BungeePermPlayer initPlayer(UUID uuid) {
		
		ProxiedPlayer p = ProxyServer.getInstance().getPlayer(uuid);
		if (p == null) throw new IllegalArgumentException("Player is not on server!");
		
		BungeePermPlayer bpp = new BungeePermPlayer(p);
		this.cache.add(bpp);
		return bpp;
		
	}
	
	private BungeePermPlayer findPlayer(UUID uuid) {
	
		for (BungeePermPlayer bpp : this.cache) {
			if (bpp.getUniqueId().equals(uuid)) return bpp;
		}
		
		return null;
		
	}
	
}
