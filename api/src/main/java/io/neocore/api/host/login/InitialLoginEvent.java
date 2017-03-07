package io.neocore.api.host.login;

import java.net.InetAddress;
import java.util.UUID;

import io.neocore.api.event.Event;
import io.neocore.api.event.Raisable;

/**
 * Very tiny, lightweight class for events when players first connect. Do not
 * query player information using the UUID because the player is not properly
 * connected yet.
 * 
 * @author treyzania
 */
@Raisable
public interface InitialLoginEvent extends Event {

	/**
	 * @return The connecting player's UUID.
	 */
	public UUID getPlayerUniqueId();

	/**
	 * @return The address that the player is connecting from.
	 */
	public InetAddress getAddress();

	/**
	 * Sets the connection to be allowed.
	 */
	public void allow();

	/**
	 * Sets the connection to be denied, with a given reason.
	 * 
	 * @param message
	 *            The reason.
	 */
	public void disallow(String message);

	/**
	 * Checks to see if the login is permitted currently.
	 * 
	 * @return <code>true</code> if permitted, <code>false</code> otherwise.
	 */
	public boolean isPermitted();

}
