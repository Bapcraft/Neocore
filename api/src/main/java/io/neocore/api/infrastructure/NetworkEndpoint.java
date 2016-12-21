package io.neocore.api.infrastructure;

import java.util.Set;

import io.neocore.api.AgentIdentity;

/**
 * Represents an actual playable server on a network that players will\
 * ultimately communicate with.
 * 
 * @author treyzania
 */
public interface NetworkEndpoint extends AgentIdentity {
	
	/**
	 * Gets a set of all of the players on the server.
	 * 
	 * @return The set of all players.
	 */
	public Set<NetworkPlayer> getPlayers();
	
	/**
	 * @return The number of players on the server.
	 */
	public default int getPlayerCount() {
		return this.getPlayers().size();
	}
	
}
