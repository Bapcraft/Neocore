package io.neocore.api.host;

import java.net.InetAddress;

import io.neocore.api.player.PlayerIdentity;

/**
 * Represents a player that has connected to the sever in the simplest form.
 * 
 * @author treyzania
 */
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
