package io.neocore.api.event.database;

import io.neocore.api.event.PlayerEvent;

public interface PostPlayerFlushToDatabaseEvent extends PlayerEvent {
	
	public FlushReason getReason();
	
}
