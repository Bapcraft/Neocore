package io.neocore.bukkit.services.permissions;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import io.neocore.api.host.permissions.PermissedPlayer;
import io.neocore.api.host.permissions.PermissionsService;
import io.neocore.bukkit.PermissionInjector;
import io.neocore.common.player.CommonPlayerManager;

public class BukkitPermsService implements PermissionsService {
	
	// apparently?  (I mean it is just a utility thing...)
	private static final PermissionInjector injector = new PermissionInjector();
	
	private List<BukkitPermPlayer> playerCache = new ArrayList<>();
	private CommonPlayerManager playerManager;
	
	public BukkitPermsService(CommonPlayerManager cpm) {
		this.playerManager = cpm;
	}
	
	@Override
	public PermissedPlayer getPlayer(UUID uuid) {
		
		BukkitPermPlayer bpp = this.findPlayer(uuid);
		
		if (bpp == null) bpp = this.initPlayer(uuid);
		
		return null;
		
	}
	
	private BukkitPermPlayer initPlayer(UUID uuid) {
		
		Player p = Bukkit.getPlayer(uuid);
		if (p == null) throw new IllegalArgumentException("Player is not on server!");
		
		BukkitPermPlayer bpp = new BukkitPermPlayer(injector, this.playerManager, p);
		this.playerCache.add(bpp);
		return bpp;
		
	}
	
	private BukkitPermPlayer findPlayer(UUID uuid) {
	
		for (BukkitPermPlayer bpp : this.playerCache) {
			if (bpp.getUniqueId().equals(uuid)) return bpp;
		}
		
		return null;
		
	}

	@Override
	public void unload(UUID uuid) {
		
		// Just remove it from the cache.
		this.playerCache.removeIf(p -> p.getUniqueId().equals(uuid));
		
	}
	
}
