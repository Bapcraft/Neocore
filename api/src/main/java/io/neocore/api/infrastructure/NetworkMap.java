package io.neocore.api.infrastructure;

import java.util.Set;

public interface NetworkMap {
	
	/**
	 * @return The frontend that players connect to.
	 */
	public ConnectionFrontend getFrontend();
	
	/**
	 * @return The set of endpoints of the network that players can play on.
	 */
	public Set<NetworkEndpoint> getEndpoints();
	
}
