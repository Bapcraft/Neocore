package io.neocore.api.event.database;

import io.neocore.api.player.NeoPlayer;
import io.neocore.api.event.Raisable;

@Raisable
public class PostReloadNeoPlayerEvent extends NeoPlayerSupportedDatabaseEvent<ReloadReason> {

	public PostReloadNeoPlayerEvent(ReloadReason r, NeoPlayer player) {
		super(r, player);
	}

}
