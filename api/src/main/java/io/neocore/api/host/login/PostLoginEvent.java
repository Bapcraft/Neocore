package io.neocore.api.host.login;

import io.neocore.api.event.PlayerEvent;

/**
 * Event for when the player has finished connecting and their data has been
 * loaded from the database.
 * 
 * @author treyzania
 *
 */
public interface PostLoginEvent extends PlayerEvent {
	
	/**
	 * Sets the message to be displayed in chat.  Setting to <code>null</code> results in no message being sent.
	 * 
	 * @param message The login message.
	 */
	public void setJoinMessage(String message);
	
	/**
	 * @return The current login message.
	 */
	public String getJoinMessage();
	
}
