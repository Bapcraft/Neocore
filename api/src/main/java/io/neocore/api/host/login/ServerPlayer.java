package io.neocore.api.host.login;

import io.neocore.api.host.ConnectingPlayer;

/**
 * Represents the object provided by the underlying server software to represent the player in the server.
 * 
 * @author treyzania
 */
public interface ServerPlayer extends ConnectingPlayer {
	
	public String getName();
	
}
