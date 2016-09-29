package io.neocore.api.host;

import io.neocore.api.event.Event;
import io.neocore.api.event.Raisable;

/**
 * Signalling event used to show that the server has finished loading.
 * 
 * @author treyzania
 */
@Raisable
public interface ServerInitializedEvent extends Event {
	
	// This is really just a signaling event.
	
}
