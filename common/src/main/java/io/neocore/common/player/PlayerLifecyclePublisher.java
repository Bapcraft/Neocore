package io.neocore.common.player;

import java.util.UUID;

import io.neocore.api.database.player.DatabasePlayer;
import io.neocore.api.event.EventManager;
import io.neocore.api.event.database.FlushReason;
import io.neocore.api.event.database.LoadReason;
import io.neocore.api.event.database.PostFlushDbPlayerEvent;
import io.neocore.api.event.database.PostLoadDbPlayerEvent;
import io.neocore.api.event.database.PostReloadDbPlayerEvent;
import io.neocore.api.event.database.PostUnloadDbPlayerEvent;
import io.neocore.api.event.database.PreFlushDbPlayerEvent;
import io.neocore.api.event.database.PreLoadDbPlayerEvent;
import io.neocore.api.event.database.PreReloadDbPlayerEvent;
import io.neocore.api.event.database.PreUnloadDbPlayerEvent;
import io.neocore.api.event.database.ReloadReason;
import io.neocore.api.event.database.UnloadReason;

public class PlayerLifecyclePublisher extends LifecycleEventPublisher<DatabasePlayer> {
	
	public PlayerLifecyclePublisher(EventManager man, CommonPlayerManager cpm) {
		super(man, cpm);
	}
	
	/**
	 * This is a looooooot of repetition, but sadly I'm not quite good enough a
	 * programmer to avoid it in Java's type system.  I'm already abstracting
	 * enough things away with clever uses of generics.  Anything else would
	 * probably be ugly and messy.
	 */
	
	@Override
	public void broadcastPreLoad(LoadReason reason, UUID uuid) {
		this.events.broadcast(new PreLoadDbPlayerEvent(reason, uuid));
	}
	
	@Override
	public void broadcastPostLoad(LoadReason reason, DatabasePlayer ident) {
		this.events.broadcast(new PostLoadDbPlayerEvent(reason, ident));
	}
	
	@Override
	public void broadcastPreReload(ReloadReason reason, UUID uuid) {
		this.events.broadcast(new PreReloadDbPlayerEvent(reason, uuid));
	}
	
	@Override
	public void broadcastPostReload(ReloadReason reason, DatabasePlayer ident) {
		this.events.broadcast(new PostReloadDbPlayerEvent(reason, ident));
	}
	
	@Override
	public void broadcastPreFlush(FlushReason reason, DatabasePlayer ident) {
		this.events.broadcast(new PreFlushDbPlayerEvent(reason, ident));
	}
	
	@Override
	public void broadcastPostFlush(FlushReason reason, DatabasePlayer ident) {
		this.events.broadcast(new PostFlushDbPlayerEvent(reason, ident));
	}
	
	@Override
	public void broadcastPreUnload(UnloadReason reason, DatabasePlayer ident) {
		this.events.broadcast(new PreUnloadDbPlayerEvent(reason, ident));
	}
	
	@Override
	public void broadcastPostUnload(UnloadReason reason, UUID uuid) {
		this.events.broadcast(new PostUnloadDbPlayerEvent(reason, uuid));
	}
	
}
