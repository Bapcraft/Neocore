package io.neocore.api.event.database;

import io.neocore.api.event.PlayerEvent;

public interface PreUnloadPlayerEvent extends PlayerEvent {
	
	public UnloadReason getReason();
	
}
