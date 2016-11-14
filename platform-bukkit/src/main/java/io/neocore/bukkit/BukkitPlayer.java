package io.neocore.bukkit;

import java.net.InetAddress;
import java.util.Date;
import java.util.UUID;

import org.bukkit.entity.Player;

import io.neocore.api.host.chat.ChattablePlayer;
import io.neocore.api.host.login.ServerPlayer;

public class BukkitPlayer implements ServerPlayer, ChattablePlayer {
	
	private final Player player;
	
	public BukkitPlayer(Player player) {
		this.player = player;
	}
	
	@Override
	public UUID getUniqueId() {
		return this.player.getUniqueId();
	}
	
	@Override
	public InetAddress getAddress() {
		return this.getAddress();
	}

	@Override
	public void kick(String message) {
		this.player.kickPlayer(message);
	}
	
	@Override
	public String getName() {
		return this.player.getName();
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

	@Override
	public Date getLoginTime() {
		return null;
	}
	
}
