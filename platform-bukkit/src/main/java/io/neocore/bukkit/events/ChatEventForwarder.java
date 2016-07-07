package io.neocore.bukkit.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import io.neocore.host.chat.ChatAcceptor;

public class ChatEventForwarder extends EventForwarder {
	
	private ChatAcceptor dest;
	
	public ChatEventForwarder() {
		
	}
	
	@EventHandler
	public void onChatEvent(AsyncPlayerChatEvent event) {
		this.dest.onChatMessage(new BukkitChatEvent(event));
	}
	
	public void setAcceptor(ChatAcceptor acc) {
		this.dest = acc;
	}
	
	public ChatAcceptor getAcceptor() {
		return this.dest;
	}
	
}
