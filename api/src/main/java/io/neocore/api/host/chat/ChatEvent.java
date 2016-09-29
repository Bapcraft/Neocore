package io.neocore.api.host.chat;

import io.neocore.api.event.Cancellable;
import io.neocore.api.event.PlayerEvent;
import io.neocore.api.event.Raisable;

/**
 * Represents a global chat message sent by a player.
 * 
 * @author treyzania
 */
@Raisable
public interface ChatEvent extends PlayerEvent, Cancellable {
	
	/**
	 * Overrides the message the player sent.
	 * 
	 * @param message The text to override with.
	 */
	public void setMessage(String message);
	
	/**
	 * Gets the message that the player sent.
	 * 
	 * @return The message.
	 */
	public String getMessage();
	
}
