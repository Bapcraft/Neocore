package io.neocore.common.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.common.base.Preconditions;

import io.neocore.api.NeocoreAPI;
import io.neocore.api.event.Event;
import io.neocore.api.event.EventBus;
import io.neocore.api.event.EventManager;
import io.neocore.api.event.NeocoreEventHandler;
import io.neocore.api.event.Raisable;
import io.neocore.api.event.RegisteredListener;
import io.neocore.api.event.SimpleListener;
import io.neocore.api.module.Module;

public class CommonEventManager extends EventManager {

	private Map<Class<? extends Event>, EventBus<?>> busses;

	public CommonEventManager() {
		this.busses = new HashMap<>();
	}

	@SuppressWarnings("unchecked") // I hope this doesn't come to bite me in the
									// ass.
	@Override
	public <T extends Event> RegisteredListener<T> registerListener(Module mod, Class<?> type, Consumer<T> listener,
			int priority) {

		Preconditions.checkNotNull(mod);
		Preconditions.checkNotNull(type);
		Preconditions.checkNotNull(listener);
		Preconditions.checkArgument(Event.class.isAssignableFrom(type),
				"You did not pass an child of " + Event.class.getName() + "!");

		// Make sure we have a bus for it already set up.
		registerEventType((Class<T>) type);

		// Find and register the listener in the bus.
		EventBus<T> bus = (EventBus<T>) this.busses.get(type);
		RegisteredListener<T> rl = bus.register(mod, listener, priority);

		// Return for convenience.
		return rl;

	}

	@Override
	public void registerListeners(Module mod, SimpleListener listener, int defaultPriority) {

		Preconditions.checkNotNull(mod);
		Preconditions.checkNotNull(listener);

		Logger log = NeocoreAPI.getLogger();

		Class<?> clazz = listener.getClass();
		Method[] methods = clazz.getMethods();

		log.info("Found " + methods.length + " potential event handlers.");

		int did = 0;
		for (Method m : methods) {

			if (m.isAnnotationPresent(NeocoreEventHandler.class)) {

				if (m.getParameterCount() != 1) {

					log.warning("Method " + clazz.getName() + "." + m.getName() + " has an @"
							+ NeocoreEventHandler.class.getSimpleName()
							+ " annotation on it, but there is not 1 parameter.");
					continue;

				}

				Class<?> type = m.getParameterTypes()[0];

				if (!Event.class.isAssignableFrom(type) || !type.isAnnotationPresent(Raisable.class)) {

					log.warning("Method " + clazz.getName() + "." + m.getName() + " is doesn't take an instance of "
							+ Event.class.getName() + " with the @" + Raisable.class.getSimpleName()
							+ " annotation on it.");
					continue;

				}

				NeocoreEventHandler neh = m.getAnnotation(NeocoreEventHandler.class);
				int priority = neh.priority() != -1 ? neh.priority() : defaultPriority;

				log.info("Registering " + m.getName() + "...");
				this.registerListener(mod, type, event -> {

					try {
						m.invoke(listener, event);
					} catch (IllegalAccessException e) {
						log.log(Level.SEVERE, "Could not fire event.", e);
					} catch (IllegalArgumentException e) {
						log.log(Level.SEVERE, "Could not fire event.", e);
					} catch (InvocationTargetException e) {
						log.log(Level.SEVERE, "Could not fire event.", e);
					}

				}, priority);

				did++;

			}

		}

		log.info("Registered " + did + " methods as event handlers for " + listener.getClass().getSimpleName()
				+ " from " + mod.getName() + ".");

	}

	@SuppressWarnings("unchecked") // I hope this doesn't come to bite me in the
									// ass.
	@Override
	public <T extends Event> EventBus<T> registerEventType(Class<T> event) {

		if (!this.busses.containsKey(event)) {

			NeocoreAPI.getLogger().fine("Creating bus for type " + event.getName() + ".");
			EventBus<T> bus = new EventBus<>();
			this.busses.put(event, bus);
			return bus;

		} else {
			return (EventBus<T>) this.busses.get(event);
		}

	}

	@Override
	public <T extends Event> void broadcast(Class<? extends Event> type, T event) {

		Preconditions.checkNotNull(type);
		Preconditions.checkNotNull(event);
		NeocoreAPI.getLogger().finer("Starting event broadcast: " + type.getName() + " + (" + event + ")");

		// Make sure we have a bus for it already set up.
		registerEventType(type);

		// Actually broadcast it.
		EventBus<?> bus = this.busses.get(type);
		if (bus != null) {
			bus.broadcast(event);
		} else {
			NeocoreAPI.getLogger().warning(
					"Tried to broadcast an event that doesn't have a bus. (" + type.getName() + " " + event + ")");
		}

	}

}
