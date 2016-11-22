package io.neocore.api.database.session;

import java.util.Date;

import io.neocore.api.infrastructure.NetworkEndpoint;

public interface EndpointMove {
	
	public void setTime(Date time);
	public Date getTime();
	
	public void setDestination(NetworkEndpoint ep);
	public NetworkEndpoint getDestination();
	
}
