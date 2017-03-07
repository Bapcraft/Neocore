package io.neocore.api.event;

import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.logging.Level;

import io.neocore.api.NeocoreAPI;
import io.neocore.api.module.Module;

/**
 * Represents a conduit for passing events to listeners. This works only on one
 * type of event.
 * 
 * @author treyzania
 *
 * @param <T>
 *            The type of events that can pass through this bus.
 */
public class EventBus<T extends Event> {

	private TreeSet<RegisteredListener<T>> listeners;

	public EventBus() {
		this.listeners = new TreeSet<>();
	}

	/**
	 * Registers a listener for an event. The higher the priority the later it
	 * will be called.
	 * 
	 * @param mod
	 *            The module that is doing the listening.
	 * @param listener
	 *            The listener itself.
	 * @param priority
	 *            The priority of the listener.
	 * @return
	 */
	public RegisteredListener<T> register(Module mod, Consumer<T> listener, int priority) {

		if (mod == null)
			throw new NullPointerException("Module cannot be null!");
		if (listener == null)
			throw new NullPointerException("Event listener cannot be null!");

		// Create the listener regisration then make sure we aren't overwriting
		// anything.
		RegisteredListener<T> rl = new RegisteredListener<>(mod, listener, priority);
		while (this.listeners.contains(rl)) {

			priority--; // Decrease the priority by one.
			rl = new RegisteredListener<>(mod, listener, priority);

			if (priority < -1000000) {

				NeocoreAPI.getLogger().warning("Something is messed up with registering listener "
						+ listener.getClass().getName() + " from " + mod.getName() + ".");
				continue;

			}

		}

		this.listeners.add(rl);
		return rl;

	}

	/**
	 * Removes the listener from the bus.
	 * 
	 * @param rl
	 *            The listener registration to remove.
	 * @return If it was present in the bus.
	 */
	public boolean unregister(RegisteredListener<T> rl) {
		return this.listeners.remove(rl);
	}

	/**
	 * Broadcasts events to the listeners in order of increasing priority.
	 * 
	 * @param event
	 *            The event to broadcast.
	 */
	@SuppressWarnings("unchecked") // I hope this doesn't come bite me in the
									// ass but I can't think of a better way of
									// doing it right now.
	public void broadcast(Event event) {

		for (RegisteredListener<T> rl : this.listeners.descendingSet()) {

			/*
			 * Really wish I could use ExoContainers here, but I don't want to
			 * bother including Jzania in this project.
			 */

			try {

				// Verification
				if (rl.listener == null)
					continue;

				// Actually invoke the event
				rl.listener.accept((T) event);

			} catch (Exception e) {
				NeocoreAPI.getLogger().log(Level.WARNING,
						String.format("Exception thrown while processing event for module %s!", rl.module.getName()),
						e);
			}

		}

	}

}
