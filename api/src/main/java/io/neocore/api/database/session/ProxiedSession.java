package io.neocore.api.database.session;

import java.util.List;

import io.neocore.api.infrastructure.NetworkEndpoint;

public interface ProxiedSession extends Session {

	public List<EndpointMove> getEndpointMoves();

	public EndpointMove createEndpointMove();

	public default NetworkEndpoint getInitialEndpoint() {
		return this.getEndpointMoves().get(0).getDestination();
	}

}
