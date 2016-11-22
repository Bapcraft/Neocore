package io.neocore.manage.client.network;

import java.util.Set;

import io.neocore.api.infrastructure.ConnectionFrontend;
import io.neocore.api.infrastructure.NetworkEndpoint;
import io.neocore.api.infrastructure.NetworkPlayer;

public class DaemonizedStandalone implements DaemonizedNetworkComponent, ConnectionFrontend, NetworkEndpoint {
	
	private String name;
	
	@Override
	public String getNetworkName() {
		return this.name;
	}

	@Override
	public String getEndpointName() {
		return this.name;
	}

	@Override
	public Set<NetworkPlayer> getPlayers() {
		return null; // TODO Figure out how to get this information reliably.
	}

}
