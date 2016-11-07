package io.neocore.common.player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;
import java.util.logging.Level;

import io.neocore.api.LoadAsync;
import io.neocore.api.NeocoreAPI;
import io.neocore.api.ServiceManager;
import io.neocore.api.ServiceType;
import io.neocore.api.database.IdentityLinkage;
import io.neocore.api.host.Scheduler;
import io.neocore.api.player.IdentityProvider;
import io.neocore.api.player.NeoPlayer;
import io.neocore.api.player.PlayerIdentity;

public class CommonPlayerManager {
	
	private Set<NeoPlayer> playerCache = new TreeSet<>();
	
	private ServiceManager serviceManager;
	private Scheduler scheduler;
	
	private Set<ServiceType> loadableServices;
	private List<ProviderContainer> providerContainers;
	
	public CommonPlayerManager(ServiceManager sm, Scheduler sched) {
		
		this.serviceManager = sm;
		this.scheduler = sched;
		
		this.loadableServices = new HashSet<>();
		this.wrapServices();
		
	}
	
	public void addService(ServiceType type) {
		
		// We have to wrap the service if we didn't already have it registered.
		if (this.loadableServices.add(type)) {
			this.addServiceWrapper(type);
		}
		
	}
	
	public void wrapServices() {
		
		this.providerContainers = new ArrayList<>();
		this.loadableServices.forEach(t -> {
			this.addServiceWrapper(t);
		});
		
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
	
	public synchronized NeoPlayer assemblePlayer(UUID uuid, Consumer<NeoPlayer> callback) {
		
		NeocoreAPI.getLogger().fine("Initializing player " + uuid + "...");
		
		NeoPlayer np = new NeoPlayer(uuid);
		CountDownLatch eventLatch = new CountDownLatch(this.providerContainers.size());
		
		// Now actually load the things.
		for (ProviderContainer container : this.providerContainers) {
			
			ProvisionResult result = container.provide(np, () -> {
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
			
			// Spawn a thread for the callback.
			if (callback != null) this.scheduler.invokeAsync(() -> callback.accept(np));
			
		});
		
		// Done.
		this.playerCache.add(np);
		return np;
		
	}
	
	/**
	 * Calls load on all asynchronously-loaded containers.
	 * 
	 * @param uuid
	 */
	public void preloadPlayer(UUID uuid, Runnable callback) {
		
		CountDownLatch latch = new CountDownLatch(this.providerContainers.size());
		
		for (ProviderContainer container : this.providerContainers) {
			
			if (container instanceof AsyncProviderContainer) {
				
				this.scheduler.invokeAsync(() -> {
					
					try {
						container.getProvider().load(uuid);
					} catch (Throwable t) {
						NeocoreAPI.getLogger().log(Level.WARNING, "Problem preloading identity for " + uuid + "!", t);
					}
					
					latch.countDown();
					
				});
				
			} else {
				
				// We need to account for the container if we didn't invoke it.
				latch.countDown();
				
			}
			
		}
		
		this.scheduler.invokeAsync(() -> {
			
			try {
				latch.await();
			} catch (InterruptedException e) {
				NeocoreAPI.getLogger().warning("Wait for containers to complete when preloading " + uuid + " interrupted.  Continuing anyways...");
			}
			
			callback.run();
			
		});
		
	}
	
	public synchronized void unloadPlayer(UUID uuid, Runnable callback) {
		
		NeocoreAPI.getLogger().fine("Unloading player " + uuid + "...");
		NeoPlayer np = this.findPlayer(uuid);
		
		if (np == null) throw new IllegalArgumentException("This player doesn't seem to be loaded! (" + uuid + ")");
		
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
			
			if (callback != null) callback.run();
			
		});
		
		// Actually purge from the cache.
		this.playerCache.removeIf(p -> p.getUniqueId().equals(uuid));
		
	}
	
}
