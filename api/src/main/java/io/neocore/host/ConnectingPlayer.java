package io.neocore.host;

import java.net.InetAddress;

public interface ConnectingPlayer {
	
	/**
	 * @return The InetAddress of the player.
	 */
	public InetAddress getAddress();
	
	/**
	 * Disconnects the player from the server.
	 * 
	 * @param message The disconnect message to display.
	 */
	public void kick(String message);
	
}
