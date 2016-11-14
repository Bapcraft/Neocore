package io.neocore.api.host.login;

import java.util.Date;

import io.neocore.api.host.ConnectingPlayer;

/**
 * Represents the object provided by the underlying server software to represent the player in the server.
 * 
 * @author treyzania
 */
public interface ServerPlayer extends ConnectingPlayer {
	
	/**
	 * @return The player's actual username.
	 */
	public String getName();
	
	/**
	 * @return The time the player logged into the server.
	 */
	public Date getLoginTime();
	
}
