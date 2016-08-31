package io.neocore.api.host.chat;

// FIXME Do we use this or do we rely on the server to deal with it.
/**
 * An object that chat events should be forwarded through.
 * 
 * @author treyzania
 */
public interface ChatAcceptor {
	
	/**
	 * Should be called whenever there is an event to be processed by micrmodules.
	 * 
	 * @param event The chat event itself.
	 */
	public void onChatMessage(ChatEvent event);
	
}
