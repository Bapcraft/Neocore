package io.neocore.api.host.proxy;

import java.net.InetSocketAddress;
import java.util.Set;

/**
 * Represents an actual playable server on a network that players will\
 * ultimately communicate with.
 * 
 * @author treyzania
 */
public abstract class NetworkEndpoint {
	
	/**
	 * Gets the name of the endpoint according to the proxy.
	 * 
	 * @return The name.
	 */
	public abstract String getName();
	
	/**
	 * Gets the address of the server according to the proxy.
	 * 
	 * @return The address.
	 */
	public abstract InetSocketAddress getAddress();
	
	/**
	 * Gets a set of all of the players on the server.
	 * 
	 * @return The set of all players.
	 */
	public abstract Set<NetworkPlayer> getPlayers();
	
	/**
	 * @return The number of players on the server.
	 */
	public int getPlayerCount() {
		return this.getPlayers().size();
	}
	
	/**
	 * Sends the NetworkPlayer to this endpoint.
	 * 
	 * @param player The player to send.
	 */
	public abstract void sendPlayer(NetworkPlayer player);
	
}
