package io.neocore.common.player;

import java.io.IOException;
import java.util.function.Consumer;

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
	public ProvisionResult load(NeoPlayer player, Consumer<LoadResult> callback) {
		
		// Invoke it in a separate thread.
		this.scheduler.invokeAsync(() -> {
			
			this.locker.blockUntilUnlocked(player.getUniqueId(), 15000L); // TODO Make configurable.
			PlayerIdentity ident = null;
			
			try {
				
				ident = this.getProvider().load(player.getUniqueId());
				
				if (ident != null) {
					
					// Call outward to the container.
					if (ident instanceof Persistent) {
						((Persistent) ident).setFlushProcedure(() -> player.flush());
					}
					
					player.addIdentity(ident);
					
				}
				
			} catch (IOException e) {
				
				if (callback != null) callback.accept(new LoadResult(ActionStatus.FAILURE, e));
				return;
				
			} catch (RuntimeException e) {
				
				if (callback != null) callback.accept(new LoadResult(ActionStatus.FAILURE, e));
				return;
				
			}
			
			if (callback != null) callback.accept(new LoadResult(ident != null ? ActionStatus.SUCCESS : ActionStatus.ABORTED, ident));
			
		});
		
		return ProvisionResult.INJECTION_THREAD_SPAWNED;
		
	}
	
	@Override
	public void flush(NeoPlayer player, Consumer<FlushResult> callback) {
		
		this.scheduler.invokeAsync(() -> {
			
			if (!this.isLinkage()) {
				
				if (callback != null) callback.accept(new FlushResult(ActionStatus.FAILURE));
				return;
				
			}
			
			try {
				
				this.locker.lock(player.getUniqueId());
				try {
					this.getProviderAsLinkage().flush(player.getUniqueId());
				} catch (IOException e) {

					if (callback != null) callback.accept(new FlushResult(ActionStatus.FAILURE));
					return;
					
				} catch (RuntimeException e) {
					
					if (callback != null) callback.accept(new FlushResult(ActionStatus.FAILURE));
					return;
					
				}

				if (callback != null) callback.accept(new FlushResult(ActionStatus.SUCCESS));
				
			} finally {
				this.locker.unlock(player.getUniqueId());
			}
			
		});
		
	}
	
	@Override
	public void unload(NeoPlayer player, Consumer<UnloadResult> callback) {
		
		this.scheduler.invokeAsync(() -> {
			
			try {
				this.getProvider().unload(player.getUniqueId());
			} catch (RuntimeException e) {
				
				if (callback != null) callback.accept(new UnloadResult(ActionStatus.FAILURE));
				return;
				
			}
			
			if (callback != null) callback.accept(new UnloadResult(ActionStatus.SUCCESS));
			
		});
		
	}

}
