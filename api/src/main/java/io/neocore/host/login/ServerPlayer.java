package io.neocore.host.login;

import io.neocore.host.ConnectingPlayer;

/**
 * Represents the object provided by the underlying server software to represent the player in the server.
 * 
 * @author treyzania
 */
public interface ServerPlayer extends ConnectingPlayer {
	
	public String getName();
	
}
