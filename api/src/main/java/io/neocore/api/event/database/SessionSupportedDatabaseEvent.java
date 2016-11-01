package io.neocore.api.event.database;

import java.util.UUID;

import io.neocore.api.database.session.SimpleSessionImpl;

public class SessionSupportedDatabaseEvent<R extends DatabaseInteractionReason> extends DatabaseIdentityEvent<R> {
	
	private SimpleSessionImpl session;
	
	public SessionSupportedDatabaseEvent(R reason, SimpleSessionImpl sess) {
		
		super(reason);
		
		this.session = sess;
		
	}

	@Override
	public UUID getUniqueId() {
		return this.getSession().getUniqueId();
	}
	
	public SimpleSessionImpl getSession() {
		return this.session;
	}
	
}
