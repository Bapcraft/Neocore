package io.neocore.api.host.chat;

import io.neocore.api.host.ConnectingPlayer;
import io.neocore.api.player.PlayerIdentity;

/**
 * Represents a player that can send and recieve chat messages.
 * 
 * @author treyzania
 */
public interface ChattablePlayer extends ConnectingPlayer {
	
	/**
	 * Sets the name that should be used in chat messages in-game.
	 * 
	 * @param name The name.
	 */
	public void setDisplayName(String name);
	
	/**
	 * @return The currently set display name.
	 */
	public String getDisplayName();
	
	/**
	 * Sends a message to the player and only that player.
	 * 
	 * @param message The message to be sent.
	 */
	public void sendMessage(String message);
	
}
