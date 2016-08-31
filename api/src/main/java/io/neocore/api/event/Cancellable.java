package io.neocore.api.event;

/**
 * Represents an event that can be cancelled.
 * 
 * @author treyzania
 *
 */
public interface Cancellable {
	
	/**
	 * Sets the method to be cancelled.  Do not call this method if you don't want to interfere with previous values.
	 * 
	 * @param cancelled If the event should be cancelled.
	 */
	public void setCancelled(boolean cancelled);
	
}
