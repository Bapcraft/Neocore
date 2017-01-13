package io.neocore.common.player;

import java.util.UUID;

import io.neocore.api.database.Persistent;
import io.neocore.api.host.Scheduler;
import io.neocore.api.player.IdentityProvider;
import io.neocore.api.player.NeoPlayer;
import io.neocore.api.player.PlayerIdentity;
import io.neocore.common.net.LockCoordinator;
import io.neocore.common.net.NullLockCoordinator;

public class AsyncProviderContainer extends ProviderContainer implements LockableContainer {
	
	private Scheduler scheduler;
	
	private IdentityProvider<?> wrapped;
	private LockCoordinator locker;
	
	public AsyncProviderContainer(IdentityProvider<?> provider, Scheduler sched) {
		
		this.wrapped = provider;
		this.scheduler = sched;
		
		this.locker = new NullLockCoordinator();
		
	}
	
	@Override
	public void overrideLockCoordinator(LockCoordinator lock) {
		this.locker = lock;
	}
	
	@Override
	public IdentityProvider<?> getProvider() {
		return this.wrapped;
	}
	
	@Override
	public ProvisionResult load(NeoPlayer player, Runnable callback) {
		
		// Invoke it in a separate thread.
		this.scheduler.invokeAsync(() -> {
			
			this.exo.invoke("Provide(" + this.getProvider().getClass().getSimpleName() + ")-Async", () -> {
				
				PlayerIdentity ident = this.loadIdentity(player.getUniqueId());
				
				if (ident != null) {
					
					// Call outward to the container.
					if (ident instanceof Persistent) {
						((Persistent) ident).setFlushProcedure(() -> player.flush());
					}
					
					player.addIdentity(ident);
					
				}
				
			});
			
			if (callback != null) callback.run();
			
		});
		
		return ProvisionResult.INJECTION_THREAD_SPAWNED;
		
	}
	
	/**
	 * Used for preloading async identities.
	 * 
	 * @param uuid The UUID to load.
	 * @return The identity corresponding to the UUID.
	 */
	public PlayerIdentity loadIdentity(UUID uuid) {
		
		this.locker.blockUntilUnlocked(uuid, 15 * 1000L); // 15 seconds FIXME Make this configurable.
		return this.getProvider().load(uuid);
		
	}
	
	@Override
	public void flush(NeoPlayer player, Runnable callback) {
		
		this.scheduler.invokeAsync(() -> {
			
			this.exo.invoke("Flush(" + this.getProvider().getClass().getSimpleName() + ")-Async", () -> {
				this.getProviderAsLinkage().flush(player.getUniqueId());
			});
			
			if (callback != null) callback.run();
			
		});
		
	}
	
	@Override
	public void unload(NeoPlayer player, Runnable callback) {
		
		this.scheduler.invokeAsync(() -> {
			
			this.exo.invoke("Unload(" + this.getProvider().getClass().getSimpleName() + ")-Async", () -> {
				this.getProvider().unload(player.getUniqueId());
			});
			
			if (callback != null) callback.run();
			
		});
		
	}

}
