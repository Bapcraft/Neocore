package io.neocore.host.proxy;

import io.neocore.event.PlayerEvent;

/**
 * An event fired when a player has fully registered with the proxy host and is
 * connecting to a downstream host.
 * 
 * @author treyzania
 */
public interface DownstreamTransferEvent extends PlayerEvent {
	
	public void setDestination(DownstreamServer dest);
	public DownstreamServer getDestination();
	
}
