package io.neocore.bukkit.services;

import org.bukkit.Bukkit;

import io.neocore.api.host.broadcast.BroadcastService;

public class BukkitBroadcastService implements BroadcastService {

	@Override
	public void broadcast(String message) {
		Bukkit.broadcastMessage(message);
	}

}
