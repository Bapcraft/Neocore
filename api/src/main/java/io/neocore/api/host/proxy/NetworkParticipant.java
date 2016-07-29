package io.neocore.api.host.proxy;

import java.util.Set;
import java.util.UUID;

import io.neocore.api.host.HostServiceProvider;

public interface NetworkParticipant extends HostServiceProvider {
	
	/**
	 * Gets the proxied player information for the specified player.
	 * 
	 * @param uuid The UUID of the player
	 * @return The proxy state information
	 */
	public NetworkPlayer getPlayer(UUID uuid);
	
	/**
	 * @return An array of all downstream servers this host can connect to.
	 */
	public Set<NetworkEndpoint> getNetworkEndpoints();
	
	/**
	 * @return A network endpoint representing <i>this</i> server.
	 */
	public NetworkEndpoint getServerEndpoint();
	
	public default String getServerNetworkName() {
		return this.getServerEndpoint().getName();
	}
	
}
