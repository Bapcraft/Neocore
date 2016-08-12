package io.neocore.api.database.session;

public enum SessionState {
	
	// Running
	ACTIVE,
	
	// Not running
	DISCONNECTED,
	BANNED,
	KICKED,
	TIMEOUT;
	
}
