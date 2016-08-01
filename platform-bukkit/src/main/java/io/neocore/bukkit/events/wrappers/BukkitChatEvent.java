package io.neocore.bukkit.events.wrappers;

import org.bukkit.event.player.AsyncPlayerChatEvent;

import io.neocore.api.host.chat.ChatEvent;
import io.neocore.api.player.NeoPlayer;

public class BukkitChatEvent implements ChatEvent {
	
	private AsyncPlayerChatEvent event;
	
	public BukkitChatEvent(AsyncPlayerChatEvent event) {
		this.event = event;
	}
	
	@Override
	public NeoPlayer getPlayer() {
		return null; // TODO Make this query player from the main cache.
	}
	
	@Override
	public void setMessage(String message) {
		this.event.setMessage(message);
	}

	@Override
	public String getMessage() {
		return this.event.getMessage();
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.event.setCancelled(cancelled);
	}
	
}
