package io.neocore.api.event.database;

import io.neocore.api.player.NeoPlayer;
import io.neocore.api.event.Raisable;

@Raisable
public class PostLoadPlayerEvent extends NeoPlayerSupportedDatabaseEvent<LoadReason> {

	public PostLoadPlayerEvent(LoadReason r, NeoPlayer player) {
		super(r, player);
	}

}
