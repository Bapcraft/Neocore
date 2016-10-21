package io.neocore.common.player;

import java.util.UUID;
import java.util.function.Consumer;

import com.treyzania.jzania.ExoContainer;

import io.neocore.api.NeocoreAPI;
import io.neocore.api.database.IdentityLinkage;
import io.neocore.api.database.PersistentPlayerIdentity;
import io.neocore.api.event.database.FlushReason;
import io.neocore.api.event.database.LoadReason;
import io.neocore.api.event.database.ReloadReason;
import io.neocore.api.event.database.UnloadReason;
import io.neocore.api.host.Scheduler;

public class DataServiceWrapper<T extends PersistentPlayerIdentity, P extends IdentityLinkage<T>> {
	
	private static final long LOCK_TIMEOUT = 5000L;
	
	private ExoContainer container;
	
	private Scheduler scheduler;
	private LifecycleEventPublisher<T> publisher;
	private P provider;
	
	private volatile LockCoordinator<T> locker;
	
	public DataServiceWrapper(Scheduler sched, LifecycleEventPublisher<T> pusher, P provider) {
			
		this.scheduler = sched;
		this.publisher = pusher;
		this.provider = provider;
		
		this.locker = new NullLockCoordinator<>(); // If anything needs to override this, deal with it.
		
		this.container = new ExoContainer(NeocoreAPI.getLogger());
		
	}
	
	public void overrideLockCoordinator(LockCoordinator<T> lock) {
		this.locker = lock;
	}
	
	public void load(UUID uuid, LoadReason reason, Consumer<T> callback) {
		
		this.scheduler.invokeAsync(() -> {
			
			this.locker.blockUntilUnlocked(uuid, LOCK_TIMEOUT);
			
			this.publisher.broadcastPreLoad(reason, uuid);
			T ident = this.provider.getPlayer(uuid);
			this.publisher.broadcastPostLoad(reason, ident);
			
			this.container.invoke("LoadCallback", () -> callback.accept(ident));
			
		});
		
	}
	
	public void load(UUID uuid, Consumer<T> callback) {
		this.load(uuid, LoadReason.OTHER, callback);
	}
	
	public void reload(UUID uuid, ReloadReason reason, Consumer<T> callback) {
		
		this.scheduler.invokeAsync(() -> {
			
			this.locker.blockUntilUnlocked(uuid, LOCK_TIMEOUT);
			
			this.publisher.broadcastPreReload(reason, uuid);
			T reloaded = this.provider.reload(uuid);
			this.publisher.broadcastPostReload(reason, reloaded);
			
			this.container.invoke("ReloadCallback", () -> callback.accept(reloaded));
			
		});
		
	}
	
	public void flush(T ident, FlushReason reason, Runnable callback) {
		
		this.scheduler.invokeAsync(() -> {
			
			this.locker.lock(ident.getUniqueId());
			
			this.publisher.broadcastPreFlush(reason, ident);
			this.provider.flush(ident);
			this.publisher.broadcastPostFlush(reason, ident);
			
			this.locker.unlock(ident.getUniqueId());
			this.container.invoke("FlushCallback", callback);
			
		});
		
	}
	
	public void processRevalidate(UUID uuid, Consumer<T> callback) {
		
		this.scheduler.invokeAsync(() -> {
			
			this.provider.invalidate(uuid);
			
			this.publisher.broadcastPreReload(ReloadReason.INVALIDATION, uuid);
			T ident = this.provider.reload(uuid);
			this.publisher.broadcastPostReload(ReloadReason.INVALIDATION, ident);
			
			this.container.invoke("RevalidateCallback", () -> callback.accept(ident));
			
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
			
			this.container.invoke("UnloadCallback", callback);
			
		});
		
	}
	
}
