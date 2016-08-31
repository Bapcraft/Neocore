package io.neocore.api.database.session;

import java.util.Date;

import io.neocore.api.host.proxy.NetworkEndpoint;

/**
 * A record of a player's transistion to a destination server at a given time.
 *  
 * @author treyzania
 */
public class EndpointMove {
	
	public final Date initiation;
	public final NetworkEndpoint destination;
	
	public EndpointMove(Date init, NetworkEndpoint dest) {
		
		this.initiation = init;
		this.destination = dest;
		
	}
	
}
