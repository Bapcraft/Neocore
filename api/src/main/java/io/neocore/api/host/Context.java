package io.neocore.api.host;

/**
 * Represents human-readable properties about the context of the host.  A host can have multiple simultaneous contexts.
 * 
 * @author treyzania
 */
public interface Context {
	
	/**
	 * @return The human-readable name of the context.
	 */
	public String getName();
	
}
