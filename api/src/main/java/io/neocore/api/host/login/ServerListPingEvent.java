package io.neocore.api.host.login;

import java.net.InetAddress;

import io.neocore.api.event.Event;

public interface ServerListPingEvent extends Event {
	
	public InetAddress getAddress();
	
	public void setMotd(String motd);
	public String getMotd();
	
	public void setDisplayedMaxPlayers(int count);
	public int getDisplayedMaxPlayers();
	
}
