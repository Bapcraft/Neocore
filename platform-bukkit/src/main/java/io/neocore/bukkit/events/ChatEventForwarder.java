package io.neocore.bukkit.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import io.neocore.api.host.chat.ChatAcceptor;
import io.neocore.bukkit.events.wrappers.BukkitChatEvent;

public class ChatEventForwarder extends EventForwarder {
	
	private ChatAcceptor dest;
	
	public ChatEventForwarder() {
		
	}
	
	public void setAcceptor(ChatAcceptor acc) {
		this.dest = acc;
	}
	
	public ChatAcceptor getAcceptor() {
		return this.dest;
	}
	
	@EventHandler
	public void onChatEvent(AsyncPlayerChatEvent event) {
		this.dest.onChatMessage(new BukkitChatEvent(event));
	}
	
}
