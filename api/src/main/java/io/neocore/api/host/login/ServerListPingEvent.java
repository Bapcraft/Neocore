package io.neocore.api.host.login;

import java.net.InetAddress;

import io.neocore.api.event.Event;

/**
 * Event fired whenever a client has requested the server's MOTD from the
 * server browser.
 * 
 * @author treyzania
 */
public interface ServerListPingEvent extends Event {
	
	/**
	 * @return The address of the client.
	 */
	public InetAddress getAddress();
	
	/**
	 * Sets the MOTD to display.
	 * 
	 * @param motd The MOTD.
	 */
	public void setMotd(String motd);
	
	/**
	 * @return The current MOTD.
	 */
	public String getMotd();
	
	/**
	 * Sets the reported max players.
	 * 
	 * @param count The max players.
	 */
	public void setDisplayedMaxPlayers(int count);
	
	/**
	 * Gets the reported max players.
	 * 
	 * @return The max players.
	 */
	public int getDisplayedMaxPlayers();
	
}
