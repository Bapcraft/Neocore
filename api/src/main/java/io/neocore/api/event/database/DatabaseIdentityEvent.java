package io.neocore.api.event.database;

import io.neocore.api.event.Event;
import io.neocore.api.player.PlayerIdentity;

public abstract class DatabaseIdentityEvent<R extends DatabaseInteractionReason> implements Event, PlayerIdentity {
	
	private R reason;
	
	public DatabaseIdentityEvent(R reason) {
		this.reason = reason;
	}
	
	public R getReason() {
		return this.reason;
	}
	
}
