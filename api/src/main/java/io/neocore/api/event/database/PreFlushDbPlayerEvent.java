package io.neocore.api.event.database;

import io.neocore.api.database.player.DatabasePlayer;
import io.neocore.api.event.Raisable;

@Raisable
public class PreFlushDbPlayerEvent extends PlayerSupportedDatabaseEvent<FlushReason> {

	public PreFlushDbPlayerEvent(FlushReason r, DatabasePlayer player) {
		super(r, player);
	}

}
