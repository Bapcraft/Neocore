package io.neocore.manage.server.infrastructure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import io.neocore.manage.proto.NeomanageProtocol.ClientMessage.PayloadCase;
import io.neocore.manage.server.handling.MessageHandler;
import io.neocore.manage.server.handling.UnsupportedHandler;

public class MessageManager {
	
	private HashMap<PayloadCase, List<MessageHandler>> handlers = new HashMap<>();
	private UnsupportedHandler fallback;
	
	public MessageManager() {
		
		this.fallback = new UnsupportedHandler();
		
	}
	
	public void registerHandler(PayloadCase type, MessageHandler hndlr) {
		
		if (!this.handlers.containsKey(type)) this.handlers.put(type, new ArrayList<>());
		this.handlers.get(type).add(hndlr);
		
	}
	
	public List<MessageHandler> getHandlers(PayloadCase c) {
		
		List<MessageHandler> handlers = this.handlers.get(c);
		return handlers != null ? handlers : Arrays.asList(this.fallback); 
		
	}
	
}
