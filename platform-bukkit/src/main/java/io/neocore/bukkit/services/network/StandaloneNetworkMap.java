package io.neocore.bukkit.services.network;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import io.neocore.api.infrastructure.ConnectionFrontend;
import io.neocore.api.infrastructure.NetworkEndpoint;
import io.neocore.api.infrastructure.NetworkMap;

public class StandaloneNetworkMap implements NetworkMap {

	private StandaloneNetworkMember self;

	public StandaloneNetworkMap(StandaloneNetworkMember self) {
		this.self = self;
	}

	@Override
	public ConnectionFrontend getFrontend() {
		return this.self;
	}

	@Override
	public Set<NetworkEndpoint> getEndpoints() {
		return new HashSet<>(Arrays.asList(this.self));
	}

}
