package io.neocore.event;

import io.neocore.database.DatabasePlayerEvent;
import io.neocore.host.HostPlayerEvent;
import io.neocore.player.DatabasePlayer;
import io.neocore.player.NeoPlayer;
import io.neocore.player.ServerPlayer;

public interface PlayerEvent extends HostPlayerEvent, DatabasePlayerEvent {
	
	public NeoPlayer getPlayer();

	@Override
	default ServerPlayer getHostPlayer() {
		return this.getPlayer().getHostPlayer();
	}

	@Override
	default DatabasePlayer getDatabasePlayer() {
		return this.getPlayer().getDatabasePlayer();
	}
	
	
	
}
