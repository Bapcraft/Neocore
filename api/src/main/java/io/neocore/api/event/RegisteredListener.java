package io.neocore.api.event;

import java.util.function.Consumer;

import io.neocore.api.module.Module;

/**
 * A record of a listener being registered in an event bus.
 * 
 * @author treyzania
 *
 * @param <T>
 */
public class RegisteredListener<T> implements Comparable<RegisteredListener<T>> {
	
	/** The module providing this event */
	public final Module module;
	
	/** The listener callback itself. */
	public final Consumer<T> listener;
	
	/** The priority, where higher values are called later. */
	public final int priority;
	
	protected RegisteredListener(Module mod, Consumer<T> listener, int priority) {
		
		this.module = mod;
		this.listener = listener;
		this.priority = priority;
		
	}

	@Override
	public int compareTo(RegisteredListener<T> o) {
		return Integer.compare(this.priority, o.priority);
	}
	
}
