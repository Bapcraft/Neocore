package io.neocore.bungee.events.wrappers;

import io.neocore.api.host.login.PostLoginEvent;
import io.neocore.api.player.NeoPlayer;

public class BungeePostLoginEvent implements PostLoginEvent {
	
	private NeoPlayer player;
	
	public BungeePostLoginEvent(NeoPlayer player) {
		this.player = player;
	}
	
	@Override
	public NeoPlayer getPlayer() {
		return this.player;
	}

	@Override
	public void setJoinMessage(String message) {
		// No message on BungeeCord.
	}

	@Override
	public String getJoinMessage() {
		return ""; // No message on BungeeCord.
	}

}
