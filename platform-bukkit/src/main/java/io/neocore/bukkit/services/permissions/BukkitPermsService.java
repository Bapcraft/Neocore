package io.neocore.bukkit.services.permissions;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import io.neocore.api.host.permissions.PermissedPlayer;
import io.neocore.api.host.permissions.PermissionsService;

public class BukkitPermsService implements PermissionsService {
	
	private Plugin plugin;
	
	private List<BukkitPermPlayer> cache = new ArrayList<>();
	
	public BukkitPermsService(Plugin plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public PermissedPlayer load(UUID uuid) {
		
		BukkitPermPlayer bpp = this.findPlayer(uuid);
		if (bpp == null) bpp = this.initPlayer(uuid);
		return bpp;
		
	}
	
	private BukkitPermPlayer initPlayer(UUID uuid) {
		
		Player p = Bukkit.getPlayer(uuid);
		if (p == null) throw new IllegalArgumentException("Player is not on server!");
		
		BukkitPermPlayer bpp = new BukkitPermPlayer(this.plugin, p);
		this.cache.add(bpp);
		return bpp;
		
	}
	
	private BukkitPermPlayer findPlayer(UUID uuid) {
	
		for (BukkitPermPlayer bpp : this.cache) {
			if (bpp.getUniqueId().equals(uuid)) return bpp;
		}
		
		return null;
		
	}

	@Override
	public void unload(UUID uuid) {
		
		// Just remove it from the cache.
		this.cache.removeIf(p -> p.getUniqueId().equals(uuid));
		
	}
	
}
