package io.neocore.manage.server.infrastructure;

import java.util.HashMap;

import io.neocore.manage.proto.NeomanageProtocol.ClientMessage.MessageCase;
import io.neocore.manage.server.handling.MessageHandler;
import io.neocore.manage.server.handling.UnregisterMessageHandler;
import io.neocore.manage.server.handling.UnsupportedHandler;

public class MessageManager {
	
	private HashMap<MessageCase, MessageHandler> handlers;
	private UnsupportedHandler fallback;
	
	public MessageManager() {
		
		this.handlers = new HashMap<>();
		this.fallback = new UnsupportedHandler();
		
		this.handlers.put(MessageCase.UNREGMESSAGE, new UnregisterMessageHandler());
		
	}
	
	public MessageHandler getHandle(MessageCase c) {
		return this.handlers.getOrDefault(c, this.fallback);
	}
	
}
