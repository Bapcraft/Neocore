package io.neocore.api.listeners;

import java.util.function.Consumer;

import io.neocore.api.module.Module;

/**
 * Interfaces that simple listeners without a lot of behavior should inherit
 * from, apparently.
 * 
 * @author treyzania
 */
public interface SimpleListener<T> extends Consumer<T> {

	/**
	 * @return The module owning this listener.
	 */
	public Module getModule();

}
