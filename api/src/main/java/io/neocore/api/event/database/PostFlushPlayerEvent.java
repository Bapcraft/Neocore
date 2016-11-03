package io.neocore.api.event.database;

import io.neocore.api.player.NeoPlayer;
import io.neocore.api.event.Raisable;

@Raisable
public class PostFlushPlayerEvent extends NeoPlayerSupportedDatabaseEvent<FlushReason> {

	public PostFlushPlayerEvent(FlushReason r, NeoPlayer player) {
		super(r, player);
	}

}
