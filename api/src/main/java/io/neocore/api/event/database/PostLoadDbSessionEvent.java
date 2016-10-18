package io.neocore.api.event.database;

import io.neocore.api.database.session.Session;
import io.neocore.api.event.Raisable;

@Raisable
public class PostLoadDbSessionEvent extends SessionSupportedDatabaseEvent<LoadReason> {

	public PostLoadDbSessionEvent(LoadReason r, Session session) {
		super(r, session);
	}

}
