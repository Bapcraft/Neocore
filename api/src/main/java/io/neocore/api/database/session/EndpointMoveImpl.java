package io.neocore.api.database.session;

import java.util.Date;

import io.neocore.api.infrastructure.NetworkEndpoint;

/**
 * A record of a player's transition to a destination server at a given time.
 *  
 * @author treyzania
 */
public class EndpointMoveImpl implements EndpointMove {
	
	public Date initiation;
	public NetworkEndpoint destination;
	
	public EndpointMoveImpl(Date init, NetworkEndpoint dest) {
		
		this.initiation = init;
		this.destination = dest;
		
	}

	@Override
	public void setTime(Date time) {
		this.initiation = time;
	}

	@Override
	public Date getTime() {
		return this.initiation;
	}

	@Override
	public void setDestination(NetworkEndpoint ep) {
		this.destination = ep;
	}

	@Override
	public NetworkEndpoint getDestination() {
		return this.destination;
	}
	
}
