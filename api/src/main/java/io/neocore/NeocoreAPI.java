package io.neocore;

/**
 * Main class for the Neocore API.
 * 
 * @author treyzania
 */
public class NeocoreAPI {
	
	protected static Neocore agent;
	
	protected static void getAgent(Neocore neo) {
		agent = neo;
	}
	
	public static Neocore getAgent() {
		return agent;
	}
	
}
