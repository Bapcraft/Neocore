package io.neocore.common.player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
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

public class CommonPlayerManager implements IdentityProvider<NeoPlayer> { // TODO Impl.
	
	private Set<NeoPlayer> playerCache = new TreeSet<>();
	
	private ServiceManager serviceManager;
	private Scheduler scheduler;
	
	private List<ProviderContainer> providerContainers;
	private boolean containersInited = false;
	
	public CommonPlayerManager(ServiceManager sm, Scheduler sched) {
		
		this.serviceManager = sm;
		this.scheduler = sched;
		
	}
	
	@Override
	public NeoPlayer load(UUID uuid) {
		
		// FIXME Make this work.
		return null;
		
	}
	
	private NeoPlayer findPlayer(UUID uuid) {
		
		for (NeoPlayer np : this.playerCache) {
			if (np.getUniqueId().equals(uuid)) return np;
		}
		
		return null;
		
	}
	
	public synchronized NeoPlayer assemblePlayer(UUID uuid, Consumer<NeoPlayer> callback) {
		
		NeocoreAPI.getLogger().fine("Initializing player " + uuid + "...");
		
		NeoPlayer np = new NeoPlayer(uuid);
		
		// Make sure these are all good to go.
		this.initProviderContainers();
		
		// Now actually load the things.
		for (ProviderContainer container : this.providerContainers) {
			
			ProvisionResult result = container.provide(np);
			NeocoreAPI.getLogger().finer(
				String.format(
					"Provision result for %s on %s was %s.",
					np.getUniqueId(),
					container.getProvider().getClass().getSimpleName(),
					result.name()
				)
			);
			
		}
		
		// Done.
		this.playerCache.add(np);
		if (callback != null) callback.accept(np);
		return np;
		
	}
	
	public NeoPlayer assemblePlayer(UUID uuid) {
		return this.assemblePlayer(uuid, null);
	}
	
	public synchronized void unloadPlayer(UUID uuid) {
		
		NeocoreAPI.getLogger().fine("Unloading player " + uuid + "...");
		NeoPlayer np = this.findPlayer(uuid);
		
		if (np == null) throw new IllegalArgumentException("This player doesn't seem to be loaded! (" + uuid + ")");
		
		for (ProviderContainer container : this.providerContainers) {
			
			PlayerIdentity ident = np.getIdentity(container.getProvisionedClass());
			
			if (ident != null) {
				container.unload(np);
			}
			
		}
		
		// Actually purge from the cache.
		this.playerCache.removeIf(p -> p.getUniqueId().equals(uuid));
		
	}
	
	public void unloadPlayer(PlayerIdentity pi) {
		this.unloadPlayer(pi.getUniqueId());
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
				
				// Ughh so much type destruction!
				DataServiceWrapper<?, ?> wrapper = new DataServiceWrapper<>(this.scheduler, new DummyLifecyclePublisher<>(), (IdentityLinkage<?>) identProvider);
				container = new AsyncProviderContainer(servClazz, wrapper, this.scheduler);
				
			} else {
				container = new DirectProviderContainer(servClazz, identProvider);
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
	
	@Override
	public Class<? extends PlayerIdentity> getIdentityClass() {
		return NeoPlayer.class;
	}
	
}
