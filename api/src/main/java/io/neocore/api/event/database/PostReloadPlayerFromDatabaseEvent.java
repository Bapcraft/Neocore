package io.neocore.api.event.database;

import io.neocore.api.event.PlayerEvent;
import io.neocore.api.event.Raisable;

@Raisable
public interface PostReloadPlayerFromDatabaseEvent extends PlayerEvent {
	
	public ReloadReason getReason();
	
}
