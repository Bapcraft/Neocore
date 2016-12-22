package io.neocore.manage.client.network;

import java.util.UUID;

import io.neocore.api.infrastructure.ConnectionFrontend;

public class DaemonizedProxy extends RemoteAgent implements ConnectionFrontend {
	
	public DaemonizedProxy(UUID id, String name, String network) {
		super(id, name, network);
	}
	
}
