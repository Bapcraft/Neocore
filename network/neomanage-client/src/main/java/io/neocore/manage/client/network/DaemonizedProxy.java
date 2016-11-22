package io.neocore.manage.client.network;

import io.neocore.api.infrastructure.ConnectionFrontend;

public class DaemonizedProxy implements DaemonizedNetworkComponent, ConnectionFrontend {
	
	private String name;
	
	public DaemonizedProxy(String name) {
		this.name = name;
	}
	
	@Override
	public String getNetworkName() {
		return this.name;
	}

}
