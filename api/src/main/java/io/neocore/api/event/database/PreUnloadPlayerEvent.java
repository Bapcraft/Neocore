package io.neocore.api.event.database;

import io.neocore.api.player.NeoPlayer;
import io.neocore.api.event.Raisable;

@Raisable
public class PreUnloadPlayerEvent extends NeoPlayerSupportedDatabaseEvent<UnloadReason> {

	public PreUnloadPlayerEvent(UnloadReason r, NeoPlayer player) {
		super(r, player);
	}

}
