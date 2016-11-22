package io.neocore.bungee.network;

import io.neocore.api.infrastructure.ConnectionFrontend;

public class BungeeFrontend implements ConnectionFrontend {
	
	private String networkName;
	
	public BungeeFrontend(String name) {
		this.networkName = name;
	}
	
	@Override
	public String getNetworkName() {
		return this.networkName;
	}
	
}
