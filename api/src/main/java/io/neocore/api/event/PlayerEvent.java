package io.neocore.api.event;

import io.neocore.api.player.NeoPlayer;

/**
 * Represents any event that involves a singular player.
 * 
 * @author treyzania
 */
public interface PlayerEvent extends Event {

	/**
	 * @return The player in the context of the event.
	 */
	public NeoPlayer getPlayer();

}
