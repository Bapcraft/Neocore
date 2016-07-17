package io.neocore.api.host.chat;

import io.neocore.api.host.HostServiceProvider;

public interface ChatProvider extends HostServiceProvider {
	
	public void setChatAcceptor(ChatAcceptor acceptor);
	public ChatAcceptor getChatAcceptor();
	
	public void sendMessageFrom(ChattablePlayer sender, String message);
	
}
