package io.neocore.api.event.database;

import io.neocore.api.database.player.DatabasePlayer;
import io.neocore.api.event.Raisable;

@Raisable
public class PostReloadDbPlayerEvent extends PlayerSupportedDatabaseEvent<ReloadReason> {

	public PostReloadDbPlayerEvent(ReloadReason r, DatabasePlayer player) {
		super(r, player);
	}

}
