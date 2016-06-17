package io.neocore.host;

import io.neocore.event.Event;
import io.neocore.player.ServerPlayer;

public interface HostPlayerEvent extends Event {
	
	public ServerPlayer getHostPlayer();
	
}
