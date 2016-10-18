package io.neocore.api.event.database;

import io.neocore.api.database.player.DatabasePlayer;
import io.neocore.api.event.Raisable;

@Raisable
public class PreUnloadDbPlayerEvent extends PlayerSupportedDatabaseEvent<UnloadReason> {

	public PreUnloadDbPlayerEvent(UnloadReason r, DatabasePlayer player) {
		super(r, player);
	}

}
