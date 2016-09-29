package io.neocore.api.event.database;

import io.neocore.api.event.PlayerEvent;
import io.neocore.api.event.Raisable;

@Raisable
public interface PrePlayerLoadFromDatabaseEvent extends PlayerEvent {
	
	public LoadReason getReason();
	
}
