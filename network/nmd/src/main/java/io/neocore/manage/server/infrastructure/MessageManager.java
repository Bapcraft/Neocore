package io.neocore.manage.server.infrastructure;

import java.util.HashMap;

import io.neocore.manage.proto.NeomanageProtocol.ClientMessage.MessageCase;
import io.neocore.manage.server.handling.MessageHandler;
import io.neocore.manage.server.handling.UnregisterMessageHandler;

public class MessageManager {
	
	private HashMap<MessageCase, MessageHandler> handlers;
	
	public MessageManager() {
		
		this.handlers = new HashMap<>();
		
		this.handlers.put(MessageCase.UNREGMESSAGE, new UnregisterMessageHandler());
		
	}
	
}
