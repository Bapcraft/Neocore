package io.neocore.host.chat;

import io.neocore.host.HostServiceProvider;
import io.neocore.player.ServerPlayer;

public interface ChatProvider extends HostServiceProvider {
	
	public void setChatAcceptor(ChatAcceptor acceptor);
	public ChatAcceptor getChatAcceptor();
	
	public void sendMessageFrom(ServerPlayer sender, String message);
	
}
