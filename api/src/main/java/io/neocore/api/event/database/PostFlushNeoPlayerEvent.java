package io.neocore.api.event.database;

import io.neocore.api.player.NeoPlayer;
import io.neocore.api.event.Raisable;

@Raisable
public class PostFlushNeoPlayerEvent extends NeoPlayerSupportedDatabaseEvent<FlushReason> {

	public PostFlushNeoPlayerEvent(FlushReason r, NeoPlayer player) {
		super(r, player);
	}

}
