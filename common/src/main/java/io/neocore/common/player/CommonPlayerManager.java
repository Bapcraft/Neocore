package io.neocore.common.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

import io.neocore.api.LoadAsync;
import io.neocore.api.NeocoreAPI;
import io.neocore.api.ServiceManager;
import io.neocore.api.ServiceType;
import io.neocore.api.database.IdentityLinkage;
import io.neocore.api.event.EventManager;
import io.neocore.api.event.database.FlushReason;
import io.neocore.api.event.database.LoadReason;
import io.neocore.api.event.database.PostFlushPlayerEvent;
import io.neocore.api.event.database.PostLoadPlayerEvent;
import io.neocore.api.event.database.PostReloadPlayerEvent;
import io.neocore.api.event.database.PostUnloadPlayerEvent;
import io.neocore.api.event.database.PreFlushPlayerEvent;
import io.neocore.api.event.database.PreLoadPlayerEvent;
import io.neocore.api.event.database.PreReloadPlayerEvent;
import io.neocore.api.event.database.PreUnloadPlayerEvent;
import io.neocore.api.event.database.ReloadReason;
import io.neocore.api.event.database.UnloadReason;
import io.neocore.api.host.Scheduler;
import io.neocore.api.player.IdentityProvider;
import io.neocore.api.player.NeoPlayer;
import io.neocore.api.player.PlayerIdentity;

public class CommonPlayerManager {
	
	protected Set<NeoPlayer> playerCache = new TreeSet<>();
	protected Map<UUID, LoadType> loadStates = new HashMap<>();
	
	private ServiceManager serviceManager;
	private EventManager eventManager;
	private Scheduler scheduler;
	
	private NetworkSync networkSync;
	
	private Set<ServiceType> loadableServices;
	private List<ProviderContainer> providerContainers;
	
	public CommonPlayerManager(ServiceManager sm, EventManager em, Scheduler sched) {
		
		this.serviceManager = sm;
		this.eventManager = em;
		this.scheduler = sched;
		
		this.networkSync = new NullNetworkSync();
		
		this.loadableServices = new HashSet<>();
		this.wrapServices();
		
	}
	
	public void overrideNetworkSync(NetworkSync override) {
		
		this.networkSync.updatePlayerList(new HashSet<>()); // Close out any inbound connections.
		this.networkSync = override;
		
		this.updateContainerLockCoordinators();
		
	}
	
	public void addService(ServiceType type) {
		
		// We have to wrap the service if we didn't already have it registered.
		if (this.loadableServices.add(type)) this.addServiceWrapper(type);
		
	}
	
	private void wrapServices() {
		
		this.providerContainers = new ArrayList<>();
		this.loadableServices.forEach(t -> this.addServiceWrapper(t));
		
		this.updateContainerLockCoordinators();
		
	}
	
	private void updateContainerLockCoordinators() {
		
		for (ProviderContainer pc : this.providerContainers) {
			if (pc instanceof LockableContainer) ((LockableContainer) pc).overrideLockCoordinator(this.networkSync.getLockCoordinator());
		}
		
	}
	
	@SuppressWarnings("unchecked")
	private void addServiceWrapper(ServiceType type) {
		
		// Validation.
		if (!IdentityProvider.class.isAssignableFrom(type.getServiceClass())) {
			
			NeocoreAPI.getLogger().warning("Tried to wrap an identity service that wasn't actually an identity service! (" + type.getServiceClass().getName() + ")");
			return;
			
		}
		
		NeocoreAPI.getLogger().finer("Making container for provider: " + type.getName());
		
		IdentityProvider<?> identProvider = (IdentityProvider<?>) this.serviceManager.getService(type).getServiceProvider();
		if (identProvider == null) {
			NeocoreAPI.getLogger().warning("No identity provider found for type " + type.getName() + "!  Ignoring...");
		}
		
		// Now we actually can initialize it.
		Class<? extends IdentityProvider<?>> servClazz = (Class<? extends IdentityProvider<?>>) type.getServiceClass();
		ProviderContainer container = null;
		if (type.getServiceClass().isAnnotationPresent(LoadAsync.class) && IdentityLinkage.class.isAssignableFrom(servClazz)) {
			container = new AsyncProviderContainer(identProvider, this.scheduler);
		} else {
			container = new DirectProviderContainer(identProvider);
		}
		
		this.providerContainers.add(container);
		
	}
	
	public LoadType getLoadType(UUID uuid) {
		return this.loadStates.get(uuid);
	}
	
	public NeoPlayer findPlayer(UUID uuid) {
		
		for (NeoPlayer np : this.playerCache) {
			if (np.getUniqueId().equals(uuid)) return np;
		}
		
		return null;
		
	}
	
	public boolean isInited(UUID uuid) {
		return this.findPlayer(uuid) != null;
	}
	
	public boolean isPopulated(UUID uuid) {
		
		NeoPlayer np = this.findPlayer(uuid);
		return np != null && np.isPopulated();
		
	}
	
	public synchronized NeoPlayer assemblePlayer(UUID uuid, LoadType type, Consumer<NeoPlayer> callback) {
		
		NeocoreAPI.getLogger().fine("Initializing player " + uuid + "...");
		this.eventManager.broadcast(new PreLoadPlayerEvent(LoadReason.OTHER, uuid)); // FIXME Reason.
		
		this.networkSync.updateSubscriptionState(uuid, true);
		
		// Check to see if we're actually going to be converting it to a fulll form.
		if (this.getLoadType(uuid) == LoadType.PRELOAD && type == LoadType.FULL) {
			
			/*
			 * We're removing our local copy because we're going to be loading
			 * from the provider caches for the full version anyways.
			 */
			this.playerCache.remove(uuid);
			
		}
		
		NeoPlayer np = new NeoPlayer(uuid);
		CountDownLatch eventLatch = new CountDownLatch(this.providerContainers.size());
		
		// Now actually load the things.
		for (ProviderContainer container : this.providerContainers) {
			
			// Don't load things that are not present on the host yet.  FIXME More context-awareness from the containers.
			if (type == LoadType.PRELOAD && container instanceof DirectProviderContainer) continue;
			
			ProvisionResult result = container.load(np, () -> {
				eventLatch.countDown();
			});
			
			NeocoreAPI.getLogger().finer(
				String.format(
					"Provision result for %s on %s was %s.",
					np.getUniqueId(),
					container.getProvider().getClass().getSimpleName(),
					result.name()
				)
			);
			
		}
		
		// Now wait for all of the containers to finish their work before calling back.
		this.scheduler.invokeAsync(() -> {
			
			try {
				eventLatch.await();
			} catch (InterruptedException e) {
				NeocoreAPI.getLogger().warning("Waiting for player assembly was interrupted, invoking callback anyways...");
			}
			
			this.eventManager.broadcast(new PostLoadPlayerEvent(LoadReason.OTHER, np)); // FIXME Reason.
			np.setPopulated();
			
			// Spawn a thread for the callback.
			if (callback != null) this.scheduler.invokeAsync(() -> callback.accept(np));
			
		});
		
		// Done.
		this.loadStates.put(uuid, type);
		this.playerCache.add(np);
		return np;
		
	}
	
	public synchronized void flushPlayer(UUID uuid, Runnable callback) {
		
		NeocoreAPI.getLogger().fine("Flushing player " + uuid + " to database...");
		NeoPlayer np = this.findPlayer(uuid);
		
		if (np == null) throw new IllegalArgumentException("This player doesn't seem to be loaded! (" + uuid + ")");
		this.eventManager.broadcast(new PreFlushPlayerEvent(FlushReason.OTHER, np)); // FIXME Reason.
		
		CountDownLatch latch = new CountDownLatch(this.providerContainers.size());
		
		for (ProviderContainer container : this.providerContainers) {
			
			// Figure out if we have an identity from that container.
			PlayerIdentity ident = np.getIdentity(container.getProvisionedClass());
			if (ident != null) {
				
				container.flush(np, () -> {
					
					// Count down the latch when we're done unloading it.
					latch.countDown();
					
				});
				
			} else {
				
				// Nothing to unload but we still need to account for it.
				latch.countDown();
				
			}
			
		}
		
		// Spawn the thread that invokes the callback once everything's unloaded.
		this.scheduler.invokeAsync(() -> {
			
			try {
				latch.await();
			} catch (InterruptedException e) {
				NeocoreAPI.getLogger().warning("Waiting for identity flushing was interrupted, broadcasting events and invoking callback anyways...");
			}
			
			this.eventManager.broadcast(new PostFlushPlayerEvent(FlushReason.OTHER, np)); // FIXME Reason.
			
			if (callback != null) callback.run();
			
		});
		
	}
	
	public synchronized void unloadPlayer(UUID uuid, Runnable callback) {
		
		NeocoreAPI.getLogger().fine("Unloading player " + uuid + "...");
		NeoPlayer np = this.findPlayer(uuid);
		
		if (np == null) throw new IllegalArgumentException("This player doesn't seem to be loaded! (" + uuid + ")");
		this.eventManager.broadcast(new PreUnloadPlayerEvent(UnloadReason.OTHER, np)); // FIXME Reason.
		
		CountDownLatch latch = new CountDownLatch(this.providerContainers.size());
		
		for (ProviderContainer container : this.providerContainers) {
			
			// Figure out if we have an identity from that container.
			PlayerIdentity ident = np.getIdentity(container.getProvisionedClass());
			if (ident != null) {
				
				container.unload(np, () -> {
					
					// Count down the latch when we're done unloading it.
					latch.countDown();
					
				});
				
			} else {
				
				// Nothing to unload but we still need to account for it.
				latch.countDown();
				
			}
			
		}
		
		// Spawn the thread that invokes the callback once everything's unloaded.
		this.scheduler.invokeAsync(() -> {
			
			try {
				latch.await();
			} catch (InterruptedException e) {
				NeocoreAPI.getLogger().warning("Waiting for identity unloading was interrupted, invoking callback anyways...");
			}
			
			this.networkSync.updateSubscriptionState(uuid, false);
			this.eventManager.broadcast(new PostUnloadPlayerEvent(UnloadReason.OTHER, uuid)); // FIXME Reason.
			
			if (callback != null) callback.run();
			
			// Actually purge from the cache.
			this.playerCache.removeIf(p -> p.getUniqueId().equals(uuid));
			this.loadStates.remove(uuid);
			
		});
		
	}
	
	public synchronized void reloadPlayer(UUID uuid, Consumer<NeoPlayer> callback) {
		
		this.eventManager.broadcast(new PreReloadPlayerEvent(ReloadReason.OTHER, uuid));
		
		LoadType type = this.loadStates.get(uuid);
		
		this.unloadPlayer(uuid, () -> {
			
			this.assemblePlayer(uuid, type, np -> {
				
				this.eventManager.broadcast(new PostReloadPlayerEvent(ReloadReason.OTHER, np));
				if (callback != null) callback.accept(np);
				
			});
			
		});
		
	}
	
}
