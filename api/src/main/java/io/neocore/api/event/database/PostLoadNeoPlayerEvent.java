package io.neocore.api.event.database;

import io.neocore.api.player.NeoPlayer;
import io.neocore.api.event.Raisable;

@Raisable
public class PostLoadNeoPlayerEvent extends NeoPlayerSupportedDatabaseEvent<LoadReason> {

	public PostLoadNeoPlayerEvent(LoadReason r, NeoPlayer player) {
		super(r, player);
	}

}
