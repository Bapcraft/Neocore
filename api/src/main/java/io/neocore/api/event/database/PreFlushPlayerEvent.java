package io.neocore.api.event.database;

import io.neocore.api.player.NeoPlayer;
import io.neocore.api.event.Raisable;

@Raisable
public class PreFlushPlayerEvent extends NeoPlayerSupportedDatabaseEvent<FlushReason> {

	public PreFlushPlayerEvent(FlushReason r, NeoPlayer player) {
		super(r, player);
	}

}
