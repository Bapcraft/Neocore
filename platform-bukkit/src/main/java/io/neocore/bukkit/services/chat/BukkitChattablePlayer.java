package io.neocore.bukkit.services.chat;

import java.util.UUID;

import org.bukkit.entity.Player;

import io.neocore.api.host.chat.ChattablePlayer;

public class BukkitChattablePlayer implements ChattablePlayer {
	
	private Player player;
	
	public BukkitChattablePlayer(Player player) {
		this.player = player;
	}
	
	@Override
	public UUID getUniqueId() {
		return this.player.getUniqueId();
	}

	@Override
	public void setDisplayName(String name) {
		this.player.setDisplayName(name);
	}

	@Override
	public String getDisplayName() {
		return this.player.getDisplayName();
	}

	@Override
	public void sendMessage(String message) {
		this.player.sendMessage(message);
	}

}
