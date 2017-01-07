package io.neocore.bukkit.services.permissions;

import java.util.UUID;
import java.util.WeakHashMap;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import io.neocore.api.host.permissions.PermissedPlayer;
import io.neocore.api.host.permissions.PermissionsService;

public class BukkitPermsService implements PermissionsService {
	
	private Plugin plugin;
	private WeakHashMap<Player, NeocorePermissibleBase> bases = new WeakHashMap<>();
	
	public BukkitPermsService(Plugin plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public PermissedPlayer load(UUID uuid) {
		return new BukkitPermPlayer(this.plugin, uuid);
	}
	
	protected void addPermissibleBaseOverride(Player p, NeocorePermissibleBase pbase) {
		this.bases.put(p, pbase);
	}
	
}
