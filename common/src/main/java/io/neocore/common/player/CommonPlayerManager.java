package io.neocore.common.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.logging.Logger;

import io.neocore.api.LoadAsync;
import io.neocore.api.NeocoreAPI;
import io.neocore.api.PlayerIoThreadingModel;
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
import io.neocore.common.net.NetworkSync;
import io.neocore.common.net.NullNetworkSync;

public class CommonPlayerManager {
	
	protected Set<NeoPlayer> playerCache = new HashSet<>();
	protected Map<UUID, List<CountDownLatch>> latches = new HashMap<>();
	
	private ServiceManager serviceManager;
	private EventManager eventManager;
	private Scheduler scheduler;
	
	private NetworkSync networkSync;
	
	private Set<ServiceType> loadableServices;
	private List<ProviderContainer> providerContainers;
	private boolean wrappersDirty = false;
	
	private PlayerIoThreadingModel ioModel;
	
	public CommonPlayerManager(ServiceManager sm, EventManager em, PlayerIoThreadingModel io, Scheduler sched) {
		
		this.serviceManager = sm;
		this.eventManager = em;
		this.scheduler = sched;
		
		this.networkSync = new NullNetworkSync();
		
		this.ioModel = io;
		
		this.loadableServices = new HashSet<>();
		this.wrapServices();
		
	}
	
	public void overrideNetworkSync(NetworkSync override) {
		
		NeocoreAPI.getLogger().fine("Got new NetworkSync to work with.");
		
		// Cleanup the old one.
		this.networkSync.updatePlayerList(new HashSet<>()); // Close out any inbound connections.
		this.networkSync.setInvalidationCallback(null);
		
		// Configure the new one.
		this.networkSync = override;
		this.networkSync.setInvalidationCallback(this::processInvalidation);
		
		this.updateContainerLockCoordinators();
		
	}
	
	public NetworkSync getNetworkSync() {
		return this.networkSync;
	}
	
	private void processInvalidation(UUID uuid) {
		
		Logger log = NeocoreAPI.getLogger();
		
		NeoPlayer player = this.findPlayer(uuid);
		log.finer("Processing invalidation for " + uuid + "...");
		
		if (player != null) {
			
			player.invalidate();
			
			if (player.isPopulated()) {
				
				this.reloadPlayer(uuid, ReloadReason.INVALIDATION, np -> {
					NeocoreAPI.getLogger().finer("Player revalidation totally complete now for player " + uuid + ".");
				});
				
			} else {
				
				log.warning("Player hasn't finished being populated, but we tried to revalidate it.  Queueing delayed thread to do it.");
				this.scheduler.invokeAsync(() -> { // We don't make this get wrapped by invokeAccordingToModel because this has to be done by forking away from the network thread anyways.
					
					try {
						Thread.sleep(100L); // TODO Make this configurable.
					} catch (InterruptedException e) {
						// ehh?
					}
					
					log.finer("Trying revalidation of player " + uuid + " again...");
					this.processInvalidation(uuid);
					
				});
				
			}
			
		} else {
			log.warning("We don't have player " + uuid + " online, ignoring invalidation!");
		}
		
	}
	
	public void addService(ServiceType type) {
		
		// We have to wrap the service if we didn't already have it registered.
		if (this.loadableServices.add(type)) this.addServiceWrapper(type);
		
	}
	
	private void wrapServices() {
		
		NeocoreAPI.getLogger().fine("Wrapping " + this.loadableServices.size() + " services for player dependency injection.");
		
		this.providerContainers = new ArrayList<>();
		this.loadableServices.forEach(t -> this.addServiceWrapper(t));
		
		this.updateContainerLockCoordinators();
		this.wrappersDirty = false;
		
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
		if (this.ioModel == PlayerIoThreadingModel.SINGLE_THREAD) {
			
			container = new DirectProviderContainer(identProvider);
			
		} else if (this.ioModel == PlayerIoThreadingModel.FORCE_ASYNC) {
			
			container = new AsyncProviderContainer(identProvider, this.scheduler);
			
		} else if (this.ioModel == PlayerIoThreadingModel.AUTO || this.ioModel == null) {
			
			if (type.getServiceClass().isAnnotationPresent(LoadAsync.class) && IdentityLinkage.class.isAssignableFrom(servClazz)) {
				container = new AsyncProviderContainer(identProvider, this.scheduler);
			} else {
				container = new DirectProviderContainer(identProvider);
			}
			
		} else {
			
			NeocoreAPI.getLogger().severe("Theading model " + this.ioModel + " not supported when trying to wrap " + type.getName() + ", falling back to single-threaded.");
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
	
	private void addCachedPlayer(NeoPlayer np) {
		
		if (np != null) {
			
			this.playerCache.add(np);
			NeocoreAPI.getLogger().finest("Cached NeoPlayer " + np.getUniqueId() + " (hashcode: " + np.hashCode() + ").");
			
		} else {
			NeocoreAPI.getLogger().warning("Tried to cache a null NeoPlayer!");
		}
		
	}
	
	private void removeCachedPlayer(NeoPlayer np) {
		
		NeocoreAPI.getLogger().finest("Removing NeoPlayer " + np.getUniqueId() + " (hashcode: " + np.hashCode() + ") from cache.");
		if (!this.playerCache.remove(np)) NeocoreAPI.getLogger().warning("Tried to remove NeoPlayer " + np.getUniqueId() + " (hc" + np.hashCode() + ") from cache, but it wasn't in the cache!");
		
	}
	
	protected void awaitPlayerPopulation(UUID uuid) {
		
		if (this.isPopulated(uuid)) return;
		
		CountDownLatch latch = new CountDownLatch(1);
		
		synchronized (this.latches) {
			
			List<CountDownLatch> here = this.latches.get(uuid);
			if (here == null) {
				
				here = new ArrayList<>();
				this.latches.put(uuid, here);
				
			}
			
			here.add(latch);
			
		}
		
		try {
			
			boolean awaitOk = latch.await(10000L, TimeUnit.MILLISECONDS);
			if (!awaitOk) {
				
				NeocoreAPI.getLogger().warning("Timeout when awaiting player population.  This is probably a bug!");
				throw new IllegalStateException("Timed out when awaiting for player population.");
				
			}
			
		} catch (InterruptedException e) {
			NeocoreAPI.getLogger().severe("Waiting for player to finish loading interruped, continuing anyways.");
		}
		
	}
	
	private void invokeAccordingToModel(Runnable r) {
		
		if (this.ioModel == PlayerIoThreadingModel.SINGLE_THREAD) {
			r.run();
		} else {
			this.scheduler.invokeAsync(r);
		}
		
	}
	
	public synchronized NeoPlayer assemblePlayer(UUID uuid, LoadReason reason, Consumer<NeoPlayer> callback) {
		
		NeocoreAPI.getLogger().fine("Initializing player " + uuid + "...");
		if (this.wrappersDirty) this.wrapServices();
		this.eventManager.broadcast(new PreLoadPlayerEvent(reason, uuid));
		
		if (reason == LoadReason.JOIN || reason == LoadReason.OTHER) this.networkSync.updateSubscriptionState(uuid, true);
		
		NeoPlayer np = new NeoPlayer(uuid);
		CountDownLatch eventLatch = new CountDownLatch(this.providerContainers.size());
		
		// Now actually load the things.
		for (ProviderContainer container : this.providerContainers) {
			
			ProvisionResult result = container.load(np, () -> {
				eventLatch.countDown();
			});
			
			NeocoreAPI.getLogger().finer(
				String.format(
					"Provision result for %s (hashcode: %s) on %s was %s.",
					np.getUniqueId(),
					np.hashCode(),
					container.getProvider().getClass().getSimpleName(),
					result.name()
				)
			);
			
		}
		
		// Now wait for all of the containers to finish their work before calling back.
		this.invokeAccordingToModel(() -> {
			
			try {
				
				eventLatch.await();
				
			} catch (InterruptedException e) {
				NeocoreAPI.getLogger().warning("Waiting for player assembly was interrupted, invoking callback anyways...");
			}
			
			this.eventManager.broadcast(new PostLoadPlayerEvent(reason, np));
			np.setPopulated();
			
			synchronized (this.latches) {
				
				// Notify all waiting threads.
				List<CountDownLatch> userLatches = this.latches.get(uuid);
				if (userLatches != null) {
					
					for (CountDownLatch latch : userLatches) {
						latch.countDown();
					}
					
					// Now remove the latch list.
					this.latches.remove(uuid);
					
				}
				
			}
			
			// Spawn a thread for the callback.
			if (callback != null) this.invokeAccordingToModel(() -> callback.accept(np));
			
		});
		
		// Set up cache.
		this.addCachedPlayer(np);
		
		// Configure the flushing procedure.
		np.setFlushProcedure(() -> flushPlayerBlockingly(np));
		
		// Now return the container while it gets populated in some other thread.
		return np;
		
	}
	
	private void flushPlayerBlockingly(NeoPlayer np) {
		
		UUID uuid = np.getUniqueId();
		CountDownLatch flushWaiter = new CountDownLatch(1);
		
		this.flushPlayer(uuid, np.isDirty() ? FlushReason.DIRTY_CLEAN : FlushReason.EXPLICIT, () -> {
			
			NeocoreAPI.getLogger().finest("NeoPlayer " + uuid + " successfully flushed.");
			flushWaiter.countDown();
			
		});
		
		try {
			
			boolean flushOk = flushWaiter.await(15000L, TimeUnit.MILLISECONDS); // FIXME Make configurable.
			if (!flushOk) {
				
				NeocoreAPI.getLogger().severe("Flushing of NeoPlayer " + uuid + " timed out.  This is probably a bug!");
				throw new IllegalStateException("Timed out when waiting for complete player flushing.");
				
			}
			
		} catch (InterruptedException e) {
			NeocoreAPI.getLogger().warning("Interrupted while waiting for flush to complete.");
		}
		
	}
	
	public synchronized void flushPlayer(UUID uuid, FlushReason reason, Runnable callback) {
		
		Logger log = NeocoreAPI.getLogger();
		log.fine("Flushing player " + uuid + " to database...");
		
		NeoPlayer np = this.findPlayer(uuid);
		
		if (np == null) throw new IllegalArgumentException("This player doesn't seem to be loaded! (" + uuid + ")");
		this.eventManager.broadcast(new PreFlushPlayerEvent(reason, np));
		
		CountDownLatch latch = new CountDownLatch(this.providerContainers.size());
		
		for (ProviderContainer container : this.providerContainers) {
			
			// Figure out if we have an identity from that container.
			PlayerIdentity ident = np.getIdentity(container.getProvisionedClass());
			if (ident != null) {
				
				container.flush(np, () -> {
					
					// Count down the latch when we're done unloading it.
					log.fine("In the process of flushing " + np.getUniqueId() + ", " + container.getClass().getSimpleName() + " of " + container.getProvisionedClass().getSimpleName() + " has finished.");
					latch.countDown();
					
				});
				
			} else {
				
				// Nothing to unload but we still need to account for it.
				log.fine("In the process of flushing " + np.getUniqueId() + ", we skipped " + container.getProvisionedClass().getSimpleName() + " of " + container.getProvisionedClass().getSimpleName() + ".");
				latch.countDown();
				
			}
			
		}
		
		// Spawn the thread that invokes the callback once everything's unloaded.
		this.invokeAccordingToModel(() -> {
			
			try {
				
				boolean flushOk = latch.await(10000L, TimeUnit.MILLISECONDS); // FIXME Make configuratble.
				if (!flushOk) {
					
					log.warning("Problem awaiting flush completion for player " + uuid + ".  This is probably a bug!");
					throw new IllegalStateException("Timed out when waiting for player flushing.");
					
				}
				
			} catch (InterruptedException e) {
				log.warning("Waiting for identity flushing was interrupted, broadcasting events and invoking callback anyways...");
			} finally {
				
				log.finest("Player " + uuid + " has finished identity population.");
				
				// Mark the player as clean.
				np.setDirty(false);
				
				// Broadcast the event in all the relevant channels.
				this.networkSync.announceInvalidation(uuid);
				this.eventManager.broadcast(new PostFlushPlayerEvent(reason, np));
				
				// Run the callback.
				log.finer("Cleanup for init of " + uuid + " complete, invoking callback.");
				if (callback != null) callback.run();
				
			}
			
		});
		
	}
	
	public synchronized void unloadPlayer(UUID uuid, UnloadReason reason, Runnable callback) {
		
		NeocoreAPI.getLogger().fine("Unloading player " + uuid + "...");
		NeoPlayer np = this.findPlayer(uuid);
		
		if (np == null) throw new IllegalArgumentException("This player doesn't seem to be loaded! (" + uuid + ")");
		this.eventManager.broadcast(new PreUnloadPlayerEvent(reason, np));
		this.removeCachedPlayer(np);
		
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
		this.invokeAccordingToModel(() -> {
			
			try {
				
				boolean awaitOk = latch.await(10000L, TimeUnit.MILLISECONDS);
				if (!awaitOk) {
					
					NeocoreAPI.getLogger().severe("Timed out when waiting for unload of " + uuid + "  This is probably a bug!");
					throw new IllegalStateException("Timed out when waiting for player unload.");
					
				}
				
			} catch (InterruptedException e) {
				NeocoreAPI.getLogger().warning("Waiting for identity unloading was interrupted, invoking callback anyways...");
			} finally {

				if (reason == UnloadReason.DISCONNECT) this.networkSync.updateSubscriptionState(uuid, false);
				this.eventManager.broadcast(new PostUnloadPlayerEvent(reason, uuid));
				
				if (callback != null) callback.run();
				
			}
			
		});
		
	}
	
	public synchronized void reloadPlayer(UUID uuid, ReloadReason reason, Consumer<NeoPlayer> callback) {
		
		NeocoreAPI.getLogger().fine("Performing revalidation for " + uuid + "...");
		
		this.eventManager.broadcast(new PreReloadPlayerEvent(reason, uuid));
		
		this.unloadPlayer(uuid, UnloadReason.OTHER, () -> {
			
			NeocoreAPI.getLogger().finer("Unload for " + uuid + " complete, reassembling...");
			
			this.assemblePlayer(uuid, LoadReason.REVALIDATE, np -> {
				
				NeocoreAPI.getLogger().fine("Revalidation for " + uuid + " complete.");
				this.eventManager.broadcast(new PostReloadPlayerEvent(reason, np));
				if (callback != null) callback.accept(np);
				
			});
			
		});
		
	}
	
}
