package io.neocore.common.player;

import java.util.UUID;

import io.neocore.api.database.PersistentPlayerIdentity;
import io.neocore.api.event.database.FlushReason;
import io.neocore.api.event.database.LoadReason;
import io.neocore.api.event.database.ReloadReason;
import io.neocore.api.event.database.UnloadReason;

public class DummyLifecyclePublisher<T extends PersistentPlayerIdentity> extends LifecycleEventPublisher<T> {

	public DummyLifecyclePublisher() {
		super(null, null);
	}

	@Override
	public void broadcastPreLoad(LoadReason reason, UUID uuid) {
		
	}

	@Override
	public void broadcastPostLoad(LoadReason reason, T ident) {
		
	}

	@Override
	public void broadcastPreReload(ReloadReason reason, UUID uuid) {
		
	}

	@Override
	public void broadcastPostReload(ReloadReason reason, T ident) {
		
	}

	@Override
	public void broadcastPreFlush(FlushReason reason, T ident) {
		
	}

	@Override
	public void broadcastPostFlush(FlushReason reason, T ident) {
		
	}

	@Override
	public void broadcastPreUnload(UnloadReason reason, T ident) {
		
	}

	@Override
	public void broadcastPostUnload(UnloadReason reason, UUID uuid) {
		
	}

}
