package io.neocore.bungee.network;

import java.util.HashSet;
import java.util.Set;

import io.neocore.api.infrastructure.ConnectionFrontend;
import io.neocore.api.infrastructure.NetworkEndpoint;
import io.neocore.api.infrastructure.NetworkMap;

public class BungeeNetworkMap implements NetworkMap {

	private BungeeFrontend frontend;
	private DownstreamWrapper downstream;

	public BungeeNetworkMap(BungeeFrontend front, DownstreamWrapper down) {

		this.frontend = front;
		this.downstream = down;

	}

	@Override
	public ConnectionFrontend getFrontend() {
		return this.frontend;
	}

	@Override
	public Set<NetworkEndpoint> getEndpoints() {

		Set<NetworkEndpoint> eps = new HashSet<>();
		eps.addAll(this.downstream.getEndpoints());
		return eps;

	}

}
