package io.neocore.api.event.database;

import io.neocore.api.event.PlayerEvent;

public interface PreReloadPlayerFromDatabaseEvent extends PlayerEvent {
	
	public ReloadReason getReason();
	
}
