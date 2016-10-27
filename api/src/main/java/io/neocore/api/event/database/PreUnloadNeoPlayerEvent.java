package io.neocore.api.event.database;

import io.neocore.api.player.NeoPlayer;
import io.neocore.api.event.Raisable;

@Raisable
public class PreUnloadNeoPlayerEvent extends NeoPlayerSupportedDatabaseEvent<UnloadReason> {

	public PreUnloadNeoPlayerEvent(UnloadReason r, NeoPlayer player) {
		super(r, player);
	}

}
