package io.neocore.bungee.services;

import java.net.InetAddress;
import java.util.Date;
import java.util.UUID;

import io.neocore.api.host.login.ServerPlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class BungeePlayer implements ServerPlayer {
	
	private ProxiedPlayer player;
	
	public BungeePlayer(ProxiedPlayer pp) {
		this.player = pp;
	}
	
	@Override
	public InetAddress getAddress() {
		return this.player.getAddress().getAddress();
	}
	
	@Override
	public void kick(String message) {
		this.player.disconnect(new TextComponent(ChatColor.translateAlternateColorCodes('&', message)));
	}
	
	@Override
	public UUID getUniqueId() {
		return this.player.getUniqueId();
	}
	
	@Override
	public String getName() {
		return this.player.getName();
	}
	
	@Override
	public Date getLoginTime() {
		return null; // FIXME
	}
	
}
