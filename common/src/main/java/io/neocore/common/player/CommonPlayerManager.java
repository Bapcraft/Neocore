package io.neocore.common.player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

import io.neocore.api.NeocoreAPI;
import io.neocore.api.ServiceManager;
import io.neocore.api.ServiceType;
import io.neocore.api.database.DatabaseService;
import io.neocore.api.database.IdentityLinkage;
import io.neocore.api.database.LoadAsync;
import io.neocore.api.host.HostService;
import io.neocore.api.host.Scheduler;
import io.neocore.api.player.IdentityProvider;
import io.neocore.api.player.NeoPlayer;
import io.neocore.api.player.PlayerIdentity;

public class CommonPlayerManager {
	
	private Set<NeoPlayer> playerCache = new TreeSet<>();
	
	private ServiceManager serviceManager;
	private Scheduler scheduler;
	
	private List<ProviderContainer> providerContainers;
	private boolean containersInited = false;
	
	public CommonPlayerManager(ServiceManager sm, Scheduler sched) {
		
		this.serviceManager = sm;
		this.scheduler = sched;
		
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
		
		// Make sure these are all good to go.
		this.initProviderContainers();
		
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
	
	public NeoPlayer assemblePlayer(UUID uuid) {
		return this.assemblePlayer(uuid, null);
	}
	
	public synchronized void unloadPlayer(UUID uuid, Runnable callback) {
		
		NeocoreAPI.getLogger().fine("Unloading player " + uuid + "...");
		NeoPlayer np = this.findPlayer(uuid);
		
		if (np == null) throw new IllegalArgumentException("This player doesn't seem to be loaded! (" + uuid + ")");
		
		List<ProviderContainer> ok = new ArrayList<>();
		for (ProviderContainer container : this.providerContainers) {
			
			PlayerIdentity ident = np.getIdentity(container.getProvisionedClass());
			if (ident != null) ok.add(container);
			
		}
		
		// Set up the waiter for the callback and start actually unloading the identites.
		CountDownLatch latch = new CountDownLatch(ok.size());
		ok.forEach(c -> c.unload(np, () -> {
			latch.countDown();
		}));
		
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
	
	public void unloadPlayer(PlayerIdentity pi) {
		this.unloadPlayer(pi.getUniqueId(), null);
	}
	
	@SuppressWarnings("unchecked")
	private synchronized void initProviderContainers() {
		
		if (this.containersInited) return;
		
		NeocoreAPI.getLogger().info("Initializing provider containers for the first time...");
		
		for (ServiceType type : getInjectedServices()) {
			
			NeocoreAPI.getLogger().finer("Making container for provider: " + type.getName());
			
			IdentityProvider<?> identProvider = (IdentityProvider<?>) this.serviceManager.getService(type).getServiceProvider();
			if (identProvider == null) {
				
				NeocoreAPI.getLogger().warning("No identity provider found for type " + type.getName() + "!  Ignoring...");
				continue;
				
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
		
		NeocoreAPI.getLogger().fine("Done!");
		
		this.containersInited = true;
		
	}
	
	private static List<ServiceType> getInjectedServices() {
		
		List<ServiceType> types = new ArrayList<>();
		types.addAll(Arrays.asList(
			HostService.LOGIN,
			HostService.PERMISSIONS,
			HostService.CHAT,
			HostService.PROXY,
			HostService.ENDPOINT,
			DatabaseService.PLAYER,
			DatabaseService.SESSION));
		
		return types;
		
	}
	
}
