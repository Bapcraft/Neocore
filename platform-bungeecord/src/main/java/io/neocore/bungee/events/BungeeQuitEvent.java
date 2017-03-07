package io.neocore.bungee.events;

import io.neocore.api.host.login.DisconnectEvent;
import io.neocore.api.player.NeoPlayer;

public class BungeeQuitEvent implements DisconnectEvent {

	private NeoPlayer player;

	public BungeeQuitEvent(NeoPlayer np) {
		this.player = np;
	}

	@Override
	public NeoPlayer getPlayer() {
		return this.player;
	}

	@Override
	public void setQuitMessage(String message) {

	}

	@Override
	public String getQuitMessage() {
		return "";
	}

}
