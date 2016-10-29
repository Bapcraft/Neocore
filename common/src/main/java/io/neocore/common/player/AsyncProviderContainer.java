package io.neocore.common.player;

import io.neocore.api.database.IdentityLinkage;
import io.neocore.api.event.database.LoadReason;
import io.neocore.api.host.Scheduler;
import io.neocore.api.player.IdentityProvider;
import io.neocore.api.player.NeoPlayer;

public class AsyncProviderContainer extends ProviderContainer {
	
	private Scheduler scheduler;
	
	private DataServiceWrapper<?, ?> wrapper;
	
	public AsyncProviderContainer(Class<? extends IdentityProvider<?>> sc, DataServiceWrapper<?, ?> dsw, Scheduler sched) {
		
		super(sc);
		
		this.scheduler = sched;
		
	}
	
	@Override
	public IdentityProvider<?> getProvider() {
		return this.wrapper.getProvider();
	}

	public boolean isLinkage() {
		return this.getProvider() instanceof IdentityLinkage;
	}
	
	public IdentityLinkage<?> getProviderAsLinkage() {
		return (IdentityLinkage<?>) this.getProvider();
	}
	
	@Override
	public ProvisionResult provide(NeoPlayer player) {
		
		// Invoke it in a separate thread.
		this.scheduler.invokeAsync(() -> {
			
			// FIXME Make this reason get specified more realistically.
			this.wrapper.load(player.getUniqueId(), LoadReason.OTHER, i -> {
				player.addIdentity(i);
			});
			
		});
		
		return ProvisionResult.THREAD_SPAWNED;
		
	}

	@Override
	public void unload(NeoPlayer player) {
		
		this.scheduler.invokeAsync(() -> {
			
			// TODO Make unloading work.  Might have to do some type reworking because there's a lot of recasting going on.
			
		});
		
	}

}
