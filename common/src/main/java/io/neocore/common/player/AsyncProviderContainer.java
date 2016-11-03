package io.neocore.common.player;

import io.neocore.api.database.IdentityLinkage;
import io.neocore.api.host.Scheduler;
import io.neocore.api.player.IdentityProvider;
import io.neocore.api.player.NeoPlayer;
import io.neocore.api.player.PlayerIdentity;

public class AsyncProviderContainer extends ProviderContainer {
	
	private Scheduler scheduler;
	
	private IdentityProvider<?> wrapper;
	private LockCoordinator locker;
	
	public AsyncProviderContainer(IdentityProvider<?> provider, Scheduler sched) {
		
		this.wrapper = provider;
		this.scheduler = sched;
		
		this.locker = new NullLockCoordinator();
		
	}
	
	public void overrideLockCoordinator(LockCoordinator lock) {
		this.locker = lock;
	}
	
	@Override
	public IdentityProvider<?> getProvider() {
		return this.getProvider();
	}
	
	public boolean isLinkage() {
		return this.getProvider() instanceof IdentityLinkage;
	}
	
	public IdentityLinkage<?> getProviderAsLinkage() {
		return (IdentityLinkage<?>) this.getProvider();
	}
	
	@Override
	public ProvisionResult provide(NeoPlayer player, Runnable callback) {
		
		// Invoke it in a separate thread.
		this.scheduler.invokeAsync(() -> {
			
			this.exo.invoke("Provide(" + this.getProvider().getClass().getSimpleName() + ")-Async", () -> {
				
				this.locker.blockUntilUnlocked(player.getUniqueId(), 15 * 1000L); // 15 seconds FIXME Make this configurable.
				PlayerIdentity ident = this.wrapper.load(player.getUniqueId());
				player.addIdentity(ident);
				
				if (callback != null) callback.run();
				
			});
			
		});
		
		return ProvisionResult.THREAD_SPAWNED;
		
	}
	
	@Override
	public void unload(NeoPlayer player) {
		
		this.scheduler.invokeAsync(() -> {
			
			// TODO
			
		});
		
	}
	
}
