package io.neocore.api.event.database;

import io.neocore.api.database.player.DatabasePlayer;
import io.neocore.api.event.Raisable;

@Raisable
public class PostFlushDbPlayerEvent extends PlayerSupportedDatabaseEvent<FlushReason> {

	public PostFlushDbPlayerEvent(FlushReason r, DatabasePlayer player) {
		super(r, player);
	}

}
