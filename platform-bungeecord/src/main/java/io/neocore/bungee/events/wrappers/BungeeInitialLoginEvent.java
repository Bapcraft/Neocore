package io.neocore.bungee.events.wrappers;

import java.net.InetAddress;
import java.util.UUID;

import io.neocore.api.host.login.InitialLoginEvent;
import net.md_5.bungee.api.event.LoginEvent;

public class BungeeInitialLoginEvent implements InitialLoginEvent {
	
	private LoginEvent event;
	
	public BungeeInitialLoginEvent(LoginEvent event) {
		this.event = event;
	}
	
	@Override
	public UUID getPlayerUniqueId() {
		return this.event.getConnection().getUniqueId();
	}

	@Override
	public InetAddress getAddress() {
		return this.event.getConnection().getAddress().getAddress();
	}

	@Override
	public void allow() {
		this.event.setCancelled(false);
	}

	@Override
	public void disallow(String message) {
		
		this.event.setCancelled(true);
		this.event.setCancelReason(message);
		
	}

	@Override
	public boolean isPermitted() {
		return !this.event.isCancelled();
	}

}
