package io.neocore.api.infrastructure;

import java.util.Set;

public interface NetworkMapService extends InfraServiceProvider {

	/**
	 * @return The network map for the network we are part of.
	 */
	public NetworkMap getLocalNetworkMap();

	/**
	 * @return A set of all networks in the organization.
	 */
	public Set<NetworkMap> getGlobalNetworkMaps();

}
