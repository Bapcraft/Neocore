package io.neocore.api.event.database;

import io.neocore.api.event.Raisable;

@Raisable
public interface PostPlayerLoadFromDatabaseEvent {
	
	public LoadReason getReason();
	
}
