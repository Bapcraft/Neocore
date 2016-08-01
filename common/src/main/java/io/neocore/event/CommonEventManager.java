package io.neocore.event;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import io.neocore.api.event.Event;
import io.neocore.api.event.EventBus;
import io.neocore.api.event.EventManager;
import io.neocore.api.module.Module;

public class CommonEventManager extends EventManager {
	
	private Map<Class<? extends Event>, EventBus<?>> busses;
	
	public CommonEventManager() {
		
		this.busses = new HashMap<>();
		
		// Set up the internal events.
		EventManager.setupNeocoreEvents(this);
		
	}
	
	@SuppressWarnings("unchecked") // I hope this doesn't come to bite me in the ass.
	@Override
	public <T extends Event> EventBus<T> registerListener(Module mod, Class<T> clazz, Consumer<T> listener, int priority) {
		
		// Find and register the listener in the bus.
		EventBus<T> bus = (EventBus<T>) this.busses.get(clazz);
		bus.register(mod, listener, priority);
		
		// Return for convenience.
		return bus;
		
	}
	
	@SuppressWarnings("unchecked") // I hope this doesn't come to bite me in the ass.
	@Override
	public <T extends Event> EventBus<T> registerEventType(Class<T> event) {
		
		EventBus<T> bus = new EventBus<>();
		
		if (this.busses.containsKey(event)) {
			bus = (EventBus<T>) this.busses.get(event);
		} else {
			this.busses.put(event, bus);
		}
		
		return bus;
		
	}
	
	@Override
	public void broadcast(Event event) {
		this.busses.get(event.getClass()).broadcast(event);
	}
	
}
