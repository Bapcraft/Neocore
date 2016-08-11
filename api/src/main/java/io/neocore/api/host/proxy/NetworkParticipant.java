package io.neocore.api.host.proxy;

import java.util.Set;
import io.neocore.api.host.HostServiceProvider;
import io.neocore.api.player.IdentityProvider;

public interface NetworkParticipant extends HostServiceProvider, IdentityProvider<NetworkPlayer> {
	
	/**
	 * @return An array of all downstream servers this host knows about.
	 */
	public Set<NetworkEndpoint> getNetworkEndpoints();
	
	/**
	 * @return A network endpoint representing <i>this</i> server.
	 */
	public NetworkEndpoint getServerEndpoint();
	
	public default String getServerNetworkName() {
		return this.getServerEndpoint().getName();
	}
	
	/**
	 * Moves the player from their current downstream server to another server. 
	 * 
	 * @param player The player
	 * @param server The server
	 */
	public void move(NetworkPlayer player, NetworkEndpoint server);
	
}
