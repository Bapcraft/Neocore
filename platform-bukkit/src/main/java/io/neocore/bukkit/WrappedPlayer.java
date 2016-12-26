package io.neocore.bukkit;

import java.net.InetAddress;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import io.neocore.api.host.ConnectingPlayer;
import net.md_5.bungee.api.ChatColor;

public class WrappedPlayer implements ConnectingPlayer {
	
	protected UUID uuid;
	
	public WrappedPlayer(UUID uuid) {
		this.uuid = uuid;
	}
	
	protected Player getPlayerOrThrow() {
		
		Server server = Bukkit.getServer(); // FIXME Breaks encapsulation.
		Player p = server.getPlayer(this.uuid);
		if (p != null) {
			return p;
		} else {
			throw new UnsupportedOperationException("Player " + this.uuid + " isn't online!");
		}
		
	}
	
	protected OfflinePlayer getOfflinePlayer() {
		
		Server server = Bukkit.getServer();
		return server.getOfflinePlayer(this.uuid);
		
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
		this.getPlayerOrThrow().kickPlayer(ChatColor.translateAlternateColorCodes('&', message));
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
