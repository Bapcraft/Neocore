package io.neocore.bukkit.services;

import java.util.HashSet;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import io.neocore.api.host.chat.ChatAcceptor;
import io.neocore.api.host.chat.ChatService;
import io.neocore.api.host.chat.ChattablePlayer;
import io.neocore.bukkit.NeocoreBukkitPlugin;
import io.neocore.bukkit.events.ChatEventForwarder;
import io.neocore.bukkit.services.chat.BukkitChattablePlayer;

public class BukkitChatService implements ChatService {
	
	private ChatEventForwarder forwarder;
	
	public BukkitChatService(ChatEventForwarder fwdr) {
		this.forwarder = fwdr;
	}
	
	@Override
	public ChattablePlayer load(UUID uuid) {
		return new BukkitChattablePlayer(uuid);
	}
	
	@Override
	public void setChatAcceptor(ChatAcceptor acceptor) {
		this.forwarder.setAcceptor(acceptor);
	}

	@Override
	public ChatAcceptor getChatAcceptor() {
		return this.forwarder.getAcceptor();
	}

	@Override
	public void sendMessageFrom(ChattablePlayer sender, String message) {
		
		// Create an event and just give it to Bukkit.  They can deal with it.
		AsyncPlayerChatEvent event = new AsyncPlayerChatEvent(true, Bukkit.getPlayer(sender.getUniqueId()), message, new HashSet<Player>(Bukkit.getOnlinePlayers()));
		NeocoreBukkitPlugin.inst.getServer().getPluginManager().callEvent(event);
		
	}
	
}
