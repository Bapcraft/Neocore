package io.neocore.bukkit.services.chat;

import java.util.UUID;

import io.neocore.api.host.chat.ChattablePlayer;
import io.neocore.bukkit.BukkitPlayer;

public class BukkitChattablePlayer extends BukkitPlayer implements ChattablePlayer {

	public BukkitChattablePlayer(UUID uuid) {
		super(uuid);
	}

}
