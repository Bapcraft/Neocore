package io.neocore.api.event.database;

import io.neocore.api.player.NeoPlayer;
import io.neocore.api.event.Raisable;

@Raisable
public class PreFlushNeoPlayerEvent extends NeoPlayerSupportedDatabaseEvent<FlushReason> {

	public PreFlushNeoPlayerEvent(FlushReason r, NeoPlayer player) {
		super(r, player);
	}

}
