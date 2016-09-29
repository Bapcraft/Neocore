package io.neocore.api.host.login;

import io.neocore.api.event.PlayerEvent;
import io.neocore.api.event.Raisable;

/**
 * Event fired when a player disconnects from the server for whichever reason.
 * 
 * @author treyzania
 */
@Raisable
public interface DisconnectEvent extends PlayerEvent {
	
	/**
	 * Sets the message to be displayed in chat.  Setting to <code>null</code> results in no message being sent.
	 * 
	 * @param message The disconnect message.
	 */
	public void setQuitMessage(String message);
	
	/**
	 * @return The current disconnect message.
	 */
	public String getQuitMessage();
	
}
