package io.neocore.database;

import io.neocore.event.Event;
import io.neocore.player.DatabasePlayer;

public interface DatabasePlayerEvent extends Event {
	
	public DatabasePlayer getDatabasePlayer();
	
}
