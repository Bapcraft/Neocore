package io.neocore.bungee.network;

import java.util.UUID;

import io.neocore.api.infrastructure.ConnectionFrontend;

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
	
}
