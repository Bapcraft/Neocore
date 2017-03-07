package io.neocore.bukkit.events.wrappers;

import java.net.InetAddress;

import io.neocore.api.host.login.ServerListPingEvent;

public class BukkitServerPingEvent implements ServerListPingEvent {

	private org.bukkit.event.server.ServerListPingEvent event;

	public BukkitServerPingEvent(org.bukkit.event.server.ServerListPingEvent event) {
		this.event = event;
	}

	@Override
	public InetAddress getAddress() {
		return this.event.getAddress();
	}

	@Override
	public void setMotd(String motd) {
		this.event.setMotd(motd);
	}

	@Override
	public String getMotd() {
		return this.event.getMotd();
	}

	@Override
	public void setDisplayedMaxPlayers(int count) {
		this.event.setMaxPlayers(count);
	}

	@Override
	public int getDisplayedMaxPlayers() {
		return this.event.getMaxPlayers();
	}

}
