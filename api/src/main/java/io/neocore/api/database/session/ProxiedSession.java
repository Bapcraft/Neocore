package io.neocore.api.database.session;

import java.util.List;

import io.neocore.api.host.proxy.NetworkEndpoint;

public interface ProxiedSession extends Session {
	
	public List<EndpointMove> getEndpointMoves();
	public void addEndpointMove(EndpointMove move);
	
	public default NetworkEndpoint getInitialEndpoint() {
		return this.getEndpointMoves().get(0).getDestination();
	}
	
}
