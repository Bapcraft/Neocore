package io.neocore.api.event.database;

import io.neocore.api.event.PlayerEvent;

public interface PrePlayerLoadFromDatabaseEvent extends PlayerEvent {
	
	public LoadReason getReason();
	
}
