package io.neocore.api.host.proxy;

import java.util.Set;
import io.neocore.api.host.HostServiceProvider;
import io.neocore.api.player.IdentityProvider;

/**
 * Parent interface for proxy services and endpoint services.  Do not inherit from directly.
 * 
 * @author treyzania
 */
public interface NetMemberProvider extends HostServiceProvider, IdentityProvider<NetworkPlayer> {
	
	/**
	 * @return An array of all downstream servers this host knows about.
	 */
	public Set<NetworkEndpoint> getNetworkEndpoints();
	
	/**
	 * Moves the player from their current downstream server to another server. 
	 * 
	 * @param player The player.
	 * @param server The server.
	 */
	public void move(NetworkPlayer player, NetworkEndpoint server);
	
	public default NetworkEndpoint findEndpoint(String name) {
		
		for (NetworkEndpoint end : this.getNetworkEndpoints()) {
			if (end.getName().equals(name)) return end;
		}
		
		return null;
		
	}
	
}
