package io.neocore.bungee.network;

import java.util.Set;
import java.util.UUID;

import io.neocore.api.infrastructure.ConnectionFrontend;
import io.neocore.api.infrastructure.NetworkPlayer;

public class BungeeFrontend implements ConnectionFrontend {

	private String networkName;

	public BungeeFrontend(String name) {
		this.networkName = name;
	}

	@Override
	public UUID getAgentId() {
		return UUID.nameUUIDFromBytes(this.networkName.getBytes());
	}

	@Override
	public String getAgentName() {
		return this.networkName;
	}

	@Override
	public String getNetworkName() {
		return this.networkName;
	}

	@Override
	public Set<NetworkPlayer> getPlayers() {
		// TODO Auto-generated method stub
		return null;
	}

}
