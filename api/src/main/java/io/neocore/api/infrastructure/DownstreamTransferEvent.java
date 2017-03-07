package io.neocore.api.infrastructure;

import io.neocore.api.event.PlayerEvent;
import io.neocore.api.event.Raisable;

/**
 * An event fired when a player has fully registered with the proxy host and is
 * connecting to a downstream host.
 * 
 * @author treyzania
 */
@Raisable
public interface DownstreamTransferEvent extends PlayerEvent {

	/**
	 * Overrides the destination of the player.
	 * 
	 * @param dest
	 *            The new destination.
	 */
	public void setDestination(NetworkEndpoint dest);

	/**
	 * @return The current expected destination of the player.
	 */
	public NetworkEndpoint getDestination();

}
