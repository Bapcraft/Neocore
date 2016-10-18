package io.neocore.api.event.database;

import io.neocore.api.database.player.DatabasePlayer;
import io.neocore.api.event.Raisable;

@Raisable
public class PostLoadDbPlayerEvent extends PlayerSupportedDatabaseEvent<LoadReason> {

	public PostLoadDbPlayerEvent(LoadReason r, DatabasePlayer player) {
		super(r, player);
	}

}
