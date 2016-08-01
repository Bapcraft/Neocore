package io.neocore.bukkit.events.wrappers;

import java.net.InetAddress;
import java.util.UUID;

import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

import io.neocore.api.host.login.InitialLoginEvent;

public class BukkitInitialLoginEvent implements InitialLoginEvent {
	
	private PlayerLoginEvent event;
	
	private boolean allowed = true;
	
	public BukkitInitialLoginEvent(PlayerLoginEvent event) {
		this.event = event;
	}
	
	@Override
	public UUID getPlayerUniqueId() {
		return this.event.getPlayer().getUniqueId();
	}
	
	@Override
	public InetAddress getAddress() {
		return this.event.getRealAddress();
	}
	
	@Override
	public void allow() {
		
		this.event.allow();
		this.allowed = true;
		
	}
	
	@Override
	public void disallow(String message) {
		
		this.event.disallow(Result.KICK_OTHER, message);
		this.allowed = false;
		
	}

	@Override
	public boolean isPermitted() {
		return this.allowed;
	}
	
}
