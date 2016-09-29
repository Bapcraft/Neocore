package io.neocore.api.event.database;

import io.neocore.api.event.PlayerEvent;

public interface PostReloadPlayerFromDatabaseEvent extends PlayerEvent {
	
	public ReloadReason getReason();
	
}
