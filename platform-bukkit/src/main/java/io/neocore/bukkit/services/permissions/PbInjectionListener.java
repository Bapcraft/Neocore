package io.neocore.bukkit.services.permissions;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PbInjectionListener implements Listener {

	public PbInjectionListener(BukkitPermsService perms) {

	}

	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerJoin(PlayerJoinEvent event) {

	}

}
