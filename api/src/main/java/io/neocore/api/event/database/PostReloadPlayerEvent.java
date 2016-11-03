package io.neocore.api.event.database;

import io.neocore.api.player.NeoPlayer;
import io.neocore.api.event.Raisable;

@Raisable
public class PostReloadPlayerEvent extends NeoPlayerSupportedDatabaseEvent<ReloadReason> {

	public PostReloadPlayerEvent(ReloadReason r, NeoPlayer player) {
		super(r, player);
	}

}
