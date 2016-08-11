package io.neocore.api.host.chat;

import java.util.UUID;

import io.neocore.api.host.HostServiceProvider;
import io.neocore.api.player.IdentityProvider;

public interface ChatService extends HostServiceProvider, IdentityProvider<ChattablePlayer> {
	
	public ChattablePlayer getPlayer(UUID uuid);
	
	public void setChatAcceptor(ChatAcceptor acceptor);
	public ChatAcceptor getChatAcceptor();
	
	public void sendMessageFrom(ChattablePlayer sender, String message);
	
}
