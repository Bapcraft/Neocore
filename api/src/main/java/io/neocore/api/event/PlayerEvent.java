package io.neocore.api.event;

import io.neocore.api.player.NeoPlayer;

public interface PlayerEvent extends Event {
	
	public NeoPlayer getPlayer();
	
}
