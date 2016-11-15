package io.neocore.api.database.session;

public enum SessionState {
	
	// Running
	/** The player is still on the server/network. */
	ACTIVE,
	
	// Not running
	/** The player disconnected on their own. */
	DISCONNECTED,
	
	/** The player was kicked because they were banned. */
	BANNED,
	
	/** The player was kicked from in-gane. */
	KICKED,
	
	/** The player timed out from the server so were "disconnected" by the server. */
	TIMEOUT,
	
	/** Placeholder for when we don't really know what happened but they're not active anymore. */
	UNKNOWN;
	
}
