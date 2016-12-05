package io.neocore.bukkit.services;

import org.bukkit.Bukkit;

import io.neocore.api.host.broadcast.BroadcastService;
import net.md_5.bungee.api.ChatColor;

public class BukkitBroadcastService implements BroadcastService {

	@Override
	public void broadcast(String message) {
		Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', message));
	}

}
