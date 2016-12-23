package io.neocore.api.infrastructure;

public interface NetMessage {
	
	/**
	 * Gets the network member that sent this message.
	 * 
	 * @return The sender
	 */
	public Networkable getSender();
	
	/**
	 * Gets the name of the channel in which the message was sent.
	 * 
	 * @return
	 */
	public String getChannelName();
	
	/**
	 * Gets the actual contents of this network message.
	 * 
	 * @return The message
	 */
	public String getData();
	
}
