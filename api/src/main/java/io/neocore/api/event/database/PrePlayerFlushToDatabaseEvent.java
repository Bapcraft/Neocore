package io.neocore.api.event.database;

import io.neocore.api.event.PlayerEvent;

public interface PrePlayerFlushToDatabaseEvent extends PlayerEvent {
	
	public FlushReason getReason();
	
}
