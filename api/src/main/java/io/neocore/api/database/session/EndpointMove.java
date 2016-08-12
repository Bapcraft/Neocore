package io.neocore.api.database.session;

import java.util.Date;

import io.neocore.api.host.proxy.NetworkEndpoint;

public class EndpointMove {
	
	public final Date initiation;
	public final NetworkEndpoint destination;
	
	public EndpointMove(Date init, NetworkEndpoint dest) {
		
		this.initiation = init;
		this.destination = dest;
		
	}
	
}
