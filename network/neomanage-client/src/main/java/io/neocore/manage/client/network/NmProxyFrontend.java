package io.neocore.manage.client.network;

import java.util.UUID;

public class NmProxyFrontend extends RemoteAgent implements NmFrontend {
	
	public NmProxyFrontend(UUID id, String name, String network) {
		super(id, name, network);
	}
	
}
