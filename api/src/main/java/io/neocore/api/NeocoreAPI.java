package io.neocore.api;

import java.util.logging.Logger;

/**
 * Main class for the Neocore API.
 * 
 * @author treyzania
 */
public class NeocoreAPI {
	
	protected static Neocore agent;
	protected static Logger logger;
	
	public static Neocore getAgent() {
		return agent;
	}
	
	public static Logger getLogger() {
		return logger;
	}
	
}
