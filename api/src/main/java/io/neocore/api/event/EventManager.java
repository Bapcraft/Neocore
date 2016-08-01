package io.neocore.api.event;

import java.util.function.Consumer;

import io.neocore.api.host.chat.ChatEvent;
import io.neocore.api.host.login.DisconnectEvent;
import io.neocore.api.host.login.InitialLoginEvent;
import io.neocore.api.host.login.PostLoginEvent;
import io.neocore.api.host.proxy.DownstreamTransferEvent;
import io.neocore.api.module.Module;

public abstract class EventManager {
	
	public abstract <T extends Event> EventBus<T> registerListener(Module mod, Class<T> clazz, Consumer<T> listener, int priority);
	public abstract <T extends Event> EventBus<T> registerEventType(Class<T> event);
	public abstract <T extends Event> void broadcast(T event);
	
	public static void setupNeocoreEvents(EventManager man) {
		
		// Connection mediation stuff
		man.registerEventType(InitialLoginEvent.class);
		man.registerEventType(PostLoginEvent.class);
		man.registerEventType(DisconnectEvent.class);
		
		// Proxy
		man.registerEventType(DownstreamTransferEvent.class);
		
		// Chat
		man.registerEventType(ChatEvent.class);
		
	}
	
}
