package io.neocore;

/**
 * Main class for the Neocore API.
 * 
 * @author treyzania
 */
public class NeocoreAPI {
	
	private static NeocoreAPI instance;
	
	private Neocore agent;
	
	private static NeocoreAPI getAPI() {
		return instance;
	}
	
	/**
	 * @return An instance of the central Neocore Prime agent.
	 */
	public static Neocore getNeocore() {
		return getAPI().agent;
	}
	
	protected static void setNeocore(Neocore neo) {
		getAPI().agent = neo;
	}
	
}
