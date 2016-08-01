package io.neocore.api.event;

import java.util.function.Consumer;

import io.neocore.api.module.Module;

public class RegisteredListener<T> implements Comparable<RegisteredListener<T>> {
	
	public final Module module;
	public final Consumer<T> listener;
	public final int priority;
	
	public RegisteredListener(Module mod, Consumer<T> listener, int priority) {
		
		this.module = mod;
		this.listener = listener;
		this.priority = priority;
		
	}

	@Override
	public int compareTo(RegisteredListener<T> o) {
		return Integer.compare(this.priority, o.priority);
	}
	
}
