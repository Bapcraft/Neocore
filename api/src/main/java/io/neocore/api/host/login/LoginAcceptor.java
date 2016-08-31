package io.neocore.api.host.login;

/**
 * Connection-related events should be sent here for processing.
 * 
 * @author treyzania
 */
public interface LoginAcceptor {
	
	/**
	 * Should be called when a player has just connected.
	 * 
	 * @param event The event.
	 */
	public void onInitialLoginEvent(InitialLoginEvent event);
	
	/**
	 * Should be called when the NeoPlayer object has been properly loaded into the game.
	 * 
	 * @param event The event.
	 */
	public void onPostLoginEvent(PostLoginEvent event);
	
	/**
	 * Should be called when the player has disconnected for any reason.
	 * 
	 * @param event The event.
	 */
	public void onDisconnectEvent(DisconnectEvent event);
	
}
