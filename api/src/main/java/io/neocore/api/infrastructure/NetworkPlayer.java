package io.neocore.api.infrastructure;

import io.neocore.api.host.ConnectingPlayer;

/**
 * Represents a player in the network.
 */
public interface NetworkPlayer extends ConnectingPlayer {

	/**
	 * @return The server that the player is currently connected to.
	 */
	public NetworkEndpoint getDownstreamServer();

}
