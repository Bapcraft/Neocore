package io.neocore.api.event.database;

import io.neocore.api.database.session.Session;
import io.neocore.api.event.Raisable;

@Raisable
public class PostReloadDbSessionEvent extends SessionSupportedDatabaseEvent<ReloadReason> {

	public PostReloadDbSessionEvent(ReloadReason r, Session session) {
		super(r, session);
	}

}
