package io.neocore.common.player;

import java.util.UUID;
import java.util.function.Consumer;

import io.neocore.api.database.IdentityLinkage;
import io.neocore.api.database.PersistentPlayerIdentity;
import io.neocore.api.event.database.FlushReason;
import io.neocore.api.event.database.LoadReason;
import io.neocore.api.event.database.ReloadReason;
import io.neocore.api.event.database.UnloadReason;
import io.neocore.api.host.Scheduler;

public class DataServiceWrapper<T extends PersistentPlayerIdentity, P extends IdentityLinkage<T>> {
	
	private Scheduler scheduler;
	private LifecycleEventPublisher<T> publisher;
	private P provider;
	
	public DataServiceWrapper(Scheduler sched, LifecycleEventPublisher<T> pusher, P provider) {
			
		this.scheduler = sched;
		this.publisher = pusher;
		this.provider = provider;
		
	}
	
	public void load(UUID uuid, LoadReason reason, Consumer<T> callback) {
		
		this.scheduler.invokeAsync(() -> {
			
			this.publisher.broadcastPreLoad(reason, uuid);
			T ident = this.provider.getPlayer(uuid);
			this.publisher.broadcastPostLoad(reason, ident);
			
			// Callback.  Does it really matter at this point, though?  The thread is already exiting.
			this.scheduler.invokeAsync(() -> callback.accept(ident));
			
		});
		
	}
	
	public void load(UUID uuid, Consumer<T> callback) {
		this.load(uuid, LoadReason.OTHER, callback);
	}
	
	public void reload(UUID uuid, ReloadReason reason, Consumer<T> callback) {
		
		this.scheduler.invokeAsync(() -> {
			
			this.publisher.broadcastPreReload(reason, uuid);
			T reloaded = this.provider.reload(uuid);
			this.publisher.broadcastPostReload(reason, reloaded);
			
			// Callback.  Does it really matter at this point, though?  The thread is already exiting.
			this.scheduler.invokeAsync(() -> callback.accept(reloaded));
			
		});
		
	}
	
	public void flush(T ident, FlushReason reason, Runnable callback) {
		
		this.scheduler.invokeAsync(() -> {
			
			this.publisher.broadcastPreFlush(reason, ident);
			this.provider.flush(ident);
			this.publisher.broadcastPostFlush(reason, ident);
			
			// Callback.  Does it really matter at this point, though?  The thread is already exiting.
			this.scheduler.invokeAsync(callback);
			
		});
		
	}
	
	public void processRevalidate(UUID uuid, Consumer<T> callback) {
		
		this.scheduler.invokeAsync(() -> {
			
			this.provider.invalidate(uuid);
			
			this.publisher.broadcastPreReload(ReloadReason.INVALIDATION, uuid);
			T ident = this.provider.reload(uuid);
			this.publisher.broadcastPostReload(ReloadReason.INVALIDATION, ident);
			
			// Callback.  Does it really matter at this point, though?  The thread it already exiting.
			this.scheduler.invokeAsync(() -> callback.accept(ident));
			
		});
		
	}

	public void gracefulUnload(T ident, UnloadReason reason, Runnable callback) {
		
		this.scheduler.invokeAsync(() -> {
			
			this.publisher.broadcastPreUnload(reason, ident);
			
			this.publisher.broadcastPreFlush(FlushReason.UNLOAD, ident);
			this.provider.flush(ident);
			this.publisher.broadcastPostFlush(FlushReason.UNLOAD, ident);
			
			// It's invalid at this point, but we only care about the UUID.
			this.provider.unload(ident.getUniqueId());
			this.publisher.broadcastPostUnload(reason, ident.getUniqueId());
			
			// Callback.  Does it really matter at this point, though?  The thread is already exiting.
			this.scheduler.invokeAsync(callback);
			
		});
		
	}
	
}
