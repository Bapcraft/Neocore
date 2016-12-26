package io.neocore.bungee.services;

import java.net.InetAddress;
import java.util.UUID;

import io.neocore.api.host.ConnectingPlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class WrappedPlayer implements ConnectingPlayer {
	
	protected UUID uuid;
	
	public WrappedPlayer(UUID uuid) {
		this.uuid = uuid;
	}
	
	protected ProxiedPlayer getPlayerOrThrow() {
		
		ProxyServer server = ProxyServer.getInstance(); // FIXME Breaks encapsulation.
		ProxiedPlayer player = server.getPlayer(this.uuid);
		if (player != null) {
			return player;
		} else {
			throw new UnsupportedOperationException("Player " + this.uuid + " isn't online!");
		}
		
	}
	
	@Override
	public UUID getUniqueId() {
		return this.uuid;
	}

	@Override
	public InetAddress getAddress() {
		return this.getPlayerOrThrow().getAddress().getAddress();
	}

	@Override
	public void kick(String message) {
		this.getPlayerOrThrow().disconnect(new TextComponent(ChatColor.translateAlternateColorCodes('&', message)));
	}

	@Override
	public boolean isOnline() {
		
		try {
			
			this.getPlayerOrThrow();
			return true;
			
		} catch (UnsupportedOperationException e) {
			return false;
		}
		
	}

}
