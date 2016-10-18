package io.neocore.api.event.database;

import java.util.UUID;

import io.neocore.api.database.session.Session;

public class SessionSupportedDatabaseEvent<R extends DatabaseInteractionReason> extends DatabaseIdentityEvent<R> {
	
	private Session session;
	
	public SessionSupportedDatabaseEvent(R reason, Session sess) {
		
		super(reason);
		
		this.session = sess;
		
	}

	@Override
	public UUID getUniqueId() {
		return this.getSession().getUniqueId();
	}
	
	public Session getSession() {
		return this.session;
	}
	
}
