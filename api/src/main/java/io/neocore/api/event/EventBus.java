package io.neocore.api.event;

import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.logging.Level;

import io.neocore.api.NeocoreAPI;
import io.neocore.api.module.Module;

public class EventBus<T extends Event> {
	
	private TreeSet<RegisteredListener<T>> listeners;
	
	public EventBus() {
		this.listeners = new TreeSet<>();
	}
	
	public RegisteredListener<T> register(Module mod, Consumer<T> listener, int priority) {
		
		if (mod == null) throw new NullPointerException("Module cannot be null!");
		if (listener == null) throw new NullPointerException("Event listener cannot be null!");
		
		// Create the listener regisration then make sure we aren't overwriting anything.
		RegisteredListener<T> rl = new RegisteredListener<>(mod, listener, priority);
		while (this.listeners.contains(rl)) {
			
			priority--; // Decrease the priority by one.
			rl = new RegisteredListener<>(mod, listener, priority);
			
		}
		
		this.listeners.add(rl);
		return rl;
		
	}
	
	public boolean unregister(RegisteredListener<T> rl) {
		return this.listeners.remove(rl);
	}
	
	@SuppressWarnings("unchecked") // I hope this doesn't come bite me in the ass but I can't think of a better way of doing it right now.
	public void broadcast(Event event) {
		
		for (RegisteredListener<T> rl : this.listeners.descendingSet()) {
			
			try {
				
				// Verification
				if (rl.listener == null) continue;
				
				// Actually invoke the event
				rl.listener.accept((T) event);
				
			} catch (Exception e) {
				NeocoreAPI.getLogger().log(Level.WARNING, String.format("Exception thrown while processing event for module %s!", rl.module.getName()), e);
			}
			
		}
		
	}
	
}
