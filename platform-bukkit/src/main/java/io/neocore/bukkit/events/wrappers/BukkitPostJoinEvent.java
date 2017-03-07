package io.neocore.bukkit.events.wrappers;

import org.bukkit.event.player.PlayerJoinEvent;

import io.neocore.api.host.login.PostLoginEvent;
import io.neocore.api.player.NeoPlayer;

public class BukkitPostJoinEvent implements PostLoginEvent {

	private PlayerJoinEvent event;
	private NeoPlayer player;

	public BukkitPostJoinEvent(PlayerJoinEvent event, NeoPlayer player) {

		this.event = event;
		this.player = player;

	}

	@Override
	public NeoPlayer getPlayer() {
		return this.player;
	}

	@Override
	public void setJoinMessage(String message) {
		this.event.setJoinMessage(message);
	}

	@Override
	public String getJoinMessage() {
		return this.event.getJoinMessage();
	}

}
