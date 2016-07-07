package io.neocore.bukkit.providers;

import org.bukkit.Bukkit;
import io.neocore.host.broadcast.BroadcastProvider;

public class BukkitBroadcastService implements BroadcastProvider {

	@Override
	public void broadcast(String message) {
		Bukkit.broadcastMessage(message);
	}

}
