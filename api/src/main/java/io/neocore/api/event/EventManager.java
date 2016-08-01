package io.neocore.api.event;

public abstract class EventManager {
	
	public abstract <T extends Event> EventBus<T> registerEventType(Class<T> event);
	public abstract <T extends Event> void broadcast(T event);
	
}
