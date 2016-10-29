package io.neocore.common.player;

import java.util.UUID;

import io.neocore.api.database.session.Session;
import io.neocore.api.event.EventManager;
import io.neocore.api.event.database.FlushReason;
import io.neocore.api.event.database.LoadReason;
import io.neocore.api.event.database.PostFlushDbSessionEvent;
import io.neocore.api.event.database.PostLoadDbSessionEvent;
import io.neocore.api.event.database.PostReloadDbSessionEvent;
import io.neocore.api.event.database.PostUnloadDbSessionEvent;
import io.neocore.api.event.database.PreFlushDbSessionEvent;
import io.neocore.api.event.database.PreLoadDbSessionEvent;
import io.neocore.api.event.database.PreReloadDbSessionEvent;
import io.neocore.api.event.database.PreUnloadDbSessionEvent;
import io.neocore.api.event.database.ReloadReason;
import io.neocore.api.event.database.UnloadReason;

public class SessionLifecyclePublisher extends LifecycleEventPublisher<Session> {
	
	public SessionLifecyclePublisher(EventManager man, CommonPlayerManager cpm) {
		super(man, cpm);
	}
	
	/**
	 * See note in `./PlayerLifecyclePublisher.java`.
	 */
	
	@Override
	public void broadcastPreLoad(LoadReason reason, UUID uuid) {
		this.events.broadcast(new PreLoadDbSessionEvent(reason, uuid));
	}
	
	@Override
	public void broadcastPostLoad(LoadReason reason, Session ident) {
		this.events.broadcast(new PostLoadDbSessionEvent(reason, ident));
	}
	
	@Override
	public void broadcastPreReload(ReloadReason reason, UUID uuid) {
		this.events.broadcast(new PreReloadDbSessionEvent(reason, uuid));
	}
	
	@Override
	public void broadcastPostReload(ReloadReason reason, Session ident) {
		this.events.broadcast(new PostReloadDbSessionEvent(reason, ident));
	}
	
	@Override
	public void broadcastPreFlush(FlushReason reason, Session ident) {
		this.events.broadcast(new PreFlushDbSessionEvent(reason, ident));
	}
	
	@Override
	public void broadcastPostFlush(FlushReason reason, Session ident) {
		this.events.broadcast(new PostFlushDbSessionEvent(reason, ident));
	}
	
	@Override
	public void broadcastPreUnload(UnloadReason reason, Session ident) {
		this.events.broadcast(new PreUnloadDbSessionEvent(reason, ident));
	}
	
	@Override
	public void broadcastPostUnload(UnloadReason reason, UUID uuid) {
		this.events.broadcast(new PostUnloadDbSessionEvent(reason, uuid));
	}
	
}
