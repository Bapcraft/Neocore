package io.neocore.bukkit.providers;

import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import io.neocore.api.host.chat.ChatAcceptor;
import io.neocore.api.host.chat.ChatProvider;
import io.neocore.api.host.chat.ChattablePlayer;
import io.neocore.bukkit.NeocoreBukkitPlugin;
import io.neocore.bukkit.events.ChatEventForwarder;

public class BukkitChatService implements ChatProvider {
	
	private ChatEventForwarder forwarder;
	
	public BukkitChatService(ChatEventForwarder fwdr) {
		this.forwarder = fwdr;
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
