package io.neocore.api.host.proxy;

import io.neocore.api.event.PlayerEvent;

/**
 * An event fired when a player has fully registered with the proxy host and is
 * connecting to a downstream host.
 * 
 * @author treyzania
 */
public interface DownstreamTransferEvent extends PlayerEvent {
	
	/**
	 * Overrides the destination of the player.
	 * 
	 * @param dest The new destination.
	 */
	public void setDestination(NetworkEndpoint dest);
	
	/**
	 * @return The current expected destination of the player.
	 */
	public NetworkEndpoint getDestination();
	
}
