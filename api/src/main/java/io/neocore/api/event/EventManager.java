package io.neocore.api.event;

import java.util.function.Consumer;

import io.neocore.api.module.Module;

public abstract class EventManager {
	
	/**
	 * Registers a listener in the relevant event bus.
	 * 
	 * @param mod The module providinng the listener.
 	 * @param clazz The event class to listen for events of.
	 * @param listener The listener itself.
	 * @param priority The priority of the listener.
	 * @return The listener registration record.
	 */
	public abstract <T extends Event> RegisteredListener<T> registerListener(Module mod, Class<T> clazz, Consumer<T> listener, int priority);
	
	/**
	 * Registers a type of event.
	 *  
	 * @param event The event interface class.
	 * @return The event bus that these events will come from.
	 */
	public abstract <T extends Event> EventBus<T> registerEventType(Class<T> event);
	
	/**
	 * Broadcasts the event to all applicable event busses.
	 * 
	 * @param type The event interface for the event.
	 * @param event The event object itself.
	 */
	public abstract <T extends Event> void broadcast(Class<? extends Event> type, T event);
	
	/**
	 * Broadcasts the event to all applicable event busses, dynamically
	 * determining the types.
	 * 
	 * @param event The event object.
	 */
	@SuppressWarnings("unchecked")
	public <T extends Event> void broadcast(T event) {
		
		Class<? extends Event> c = event.getClass();
		
		if (!isRaisableEvent(c)) {
			
			for (Class<?> inter : c.getInterfaces()) {
				if (isRaisableEvent(inter) && Event.class.isAssignableFrom(inter)) c = (Class<? extends Event>) inter;
			}
			
		}
		
		this.broadcast(c, event);
		
	}
	
	private static boolean isRaisableEvent(Class<?> c) {
		return c.isAnnotationPresent(Raisable.class);
	}
	
}
