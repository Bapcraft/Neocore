package io.neocore.api.infrastructure;

import java.util.function.Consumer;

public interface NetworkChannelService extends InfraServiceProvider {
	
	/**
	 * Queue a message to be broadcast across the network, then invokes a
	 * callback in another thread.
	 * 
	 * @param channel The message channel
	 * @param message The message to be sent
	 * @param onSend A callback to be invoked once the message is sent
	 */
	public void queueMessage(String channel, String message, Runnable onSend);
	
	/**
	 * Queue a message to be broadcast across the network.
	 * 
	 * @param channel The message channel
	 * @param message The message to be sent
	 */
	public default void queueMessage(String channel, String message) {
		this.queueMessage(channel, message, null);
	}
	
	/**
	 * Registers a handler for a specific channel.
	 * 
	 * @param channel The channel to register the handler on
	 * @param handler The handler
	 */
	public void registerChannelHandler(String channel, Consumer<NetMessage> handler);
	
	/**
	 * Registers a general handler that accepts all messages.
	 * 
	 * @param handler The handler
	 */
	public void registerGeneralHandler(Consumer<NetMessage> handler);
	
	/**
	 * Removes a handler, of either type, from the registrations.
	 * 
	 * @param handler The already-registered handler to remove
	 */
	public void unregisterHandler(Consumer<NetMessage> handler);
	
}
