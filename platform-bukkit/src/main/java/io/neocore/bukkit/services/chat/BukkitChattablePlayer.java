package io.neocore.bukkit.services.chat;

import java.util.UUID;

import org.bukkit.ChatColor;
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
		this.player.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
	}

	@Override
	public String getDisplayName() {
		return ChatColor.stripColor(this.player.getDisplayName()); // FIXME Untranslate chat colors.
	}

	@Override
	public void sendMessage(String message) {
		this.player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
	}

}
