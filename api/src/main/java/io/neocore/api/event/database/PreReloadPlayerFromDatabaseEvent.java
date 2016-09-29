package io.neocore.api.event.database;

import io.neocore.api.event.PlayerEvent;
import io.neocore.api.event.Raisable;

@Raisable
public interface PreReloadPlayerFromDatabaseEvent extends PlayerEvent {
	
	public ReloadReason getReason();
	
}
