package io.neocore.bukkit.events.wrappers;

import org.bukkit.event.player.PlayerQuitEvent;

import io.neocore.api.host.login.DisconnectEvent;
import io.neocore.api.player.NeoPlayer;

public class BukkitQuitEvent implements DisconnectEvent {
	
	private PlayerQuitEvent event;
	private NeoPlayer player;
	
	public BukkitQuitEvent(PlayerQuitEvent event, NeoPlayer player) {
		
		this.event = event;
		this.player = player;
		
	}
	
	@Override
	public NeoPlayer getPlayer() {
		return this.player;
	}

	@Override
	public void setQuitMessage(String message) {
		this.event.setQuitMessage(message);
	}

	@Override
	public String getQuitMessage() {
		return this.event.getQuitMessage();
	}

}
