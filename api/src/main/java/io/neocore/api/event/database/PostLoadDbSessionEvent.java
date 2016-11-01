package io.neocore.api.event.database;

import io.neocore.api.database.session.SimpleSessionImpl;
import io.neocore.api.event.Raisable;

@Raisable
public class PostLoadDbSessionEvent extends SessionSupportedDatabaseEvent<LoadReason> {

	public PostLoadDbSessionEvent(LoadReason r, SimpleSessionImpl session) {
		super(r, session);
	}

}
