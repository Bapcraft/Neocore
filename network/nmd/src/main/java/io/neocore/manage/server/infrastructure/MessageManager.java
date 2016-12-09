package io.neocore.manage.server.infrastructure;

import java.util.HashMap;

import io.neocore.manage.proto.NeomanageProtocol.ClientMessage.PayloadCase;
import io.neocore.manage.server.handling.MessageHandler;
import io.neocore.manage.server.handling.UnregisterMessageHandler;
import io.neocore.manage.server.handling.UnsupportedHandler;

public class MessageManager {
	
	private HashMap<PayloadCase, MessageHandler> handlers;
	private UnsupportedHandler fallback;
	
	public MessageManager() {
		
		this.handlers = new HashMap<>();
		this.fallback = new UnsupportedHandler();
		
		this.handlers.put(PayloadCase.UNREGCLIENT, new UnregisterMessageHandler());
		
	}
	
	public MessageHandler getHandle(PayloadCase c) {
		return this.handlers.getOrDefault(c, this.fallback);
	}
	
}
