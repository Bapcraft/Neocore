package io.neocore.host;

import java.net.InetAddress;

import io.neocore.player.PlayerIdentity;

public interface ConnectingPlayer extends PlayerIdentity {
	
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
