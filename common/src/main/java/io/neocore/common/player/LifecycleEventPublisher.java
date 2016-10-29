package io.neocore.common.player;

import java.util.UUID;

import io.neocore.api.database.PersistentPlayerIdentity;
import io.neocore.api.event.EventManager;
import io.neocore.api.event.database.FlushReason;
import io.neocore.api.event.database.LoadReason;
import io.neocore.api.event.database.ReloadReason;
import io.neocore.api.event.database.UnloadReason;

public abstract class LifecycleEventPublisher<T extends PersistentPlayerIdentity> {
	
	protected EventManager events;
	protected CommonPlayerManager players;
	
	public LifecycleEventPublisher(EventManager man, CommonPlayerManager players) {
		
		this.events = man;
		
		this.players = players;
		
	}
	
	public abstract void broadcastPreLoad(LoadReason reason, UUID uuid);
	public abstract void broadcastPostLoad(LoadReason reason, T ident);
	
	public abstract void broadcastPreReload(ReloadReason reason, UUID uuid);
	public abstract void broadcastPostReload(ReloadReason reason, T ident);
	
	public abstract void broadcastPreFlush(FlushReason reason, T ident);
	public abstract void broadcastPostFlush(FlushReason reason, T ident);
	
	public abstract void broadcastPreUnload(UnloadReason reason, T ident);
	public abstract void broadcastPostUnload(UnloadReason reason, UUID uuid);
	
}
