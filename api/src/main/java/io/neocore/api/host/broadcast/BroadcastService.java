package io.neocore.api.host.broadcast;

import io.neocore.api.host.HostServiceProvider;

public interface BroadcastService extends HostServiceProvider {

	/**
	 * Broadcasts a message to the whole server.
	 * 
	 * @param message
	 *            The message to broadcast.
	 */
	public void broadcast(String message);

}
