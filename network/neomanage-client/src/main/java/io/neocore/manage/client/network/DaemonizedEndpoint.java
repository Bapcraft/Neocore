package io.neocore.manage.client.network;

import java.util.Set;

import io.neocore.api.infrastructure.NetworkEndpoint;
import io.neocore.api.infrastructure.NetworkPlayer;

public class DaemonizedEndpoint implements DaemonizedNetworkComponent, NetworkEndpoint {
	
	private String networkName, endpointName;
	
	public DaemonizedEndpoint(String net, String ep) {
		
		this.networkName = net;
		this.endpointName = ep;
		
	}
	
	@Override
	public String getNetworkName() {
		return this.networkName;
	}

	@Override
	public String getEndpointName() {
		return this.endpointName;
	}

	@Override
	public Set<NetworkPlayer> getPlayers() {
		return null; // TODO Figure out how to get this information reliably.
	}

}
