package io.neocore.manage.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.treyzania.jzania.ExoContainer;

import io.neocore.api.NeocoreAPI;

public class LockManager {

	private ExoContainer container;

	private Set<UUID> lockedUuids;
	private Map<UUID, List<Runnable>> callbackMap;

	public LockManager() {

		this.lockedUuids = new HashSet<>();
		this.callbackMap = new HashMap<>();

		this.container = new ExoContainer(NeocoreAPI.getLogger());

	}

	public void lock(UUID uuid) {

		this.lockedUuids.add(uuid);

		// Prep the callback map for adding things.
		if (!this.callbackMap.containsKey(uuid)) {
			this.callbackMap.put(uuid, new ArrayList<>());
		}

	}

	public void release(UUID uuid) {

		// Invoke everything.
		List<Runnable> callbacks = this.callbackMap.get(uuid);
		callbacks.forEach(r -> {
			this.container.invoke("UnlockRelease", r);
		});

		// Now reset.
		this.lockedUuids.remove(uuid);
		callbacks.clear();

	}

	public boolean isLocked(UUID uuid) {
		return this.lockedUuids.contains(uuid);
	}

	public void queue(UUID uuid, Runnable callback) {

		if (this.isLocked(uuid)) {
			this.callbackMap.get(uuid).add(callback);
		} else {
			this.container.invoke("LockImmediateReturn", callback);
		}

	}

}
