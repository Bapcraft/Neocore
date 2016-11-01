package io.neocore.api.event.database;

import io.neocore.api.database.session.SimpleSessionImpl;
import io.neocore.api.event.Raisable;

@Raisable
public class PostReloadDbSessionEvent extends SessionSupportedDatabaseEvent<ReloadReason> {

	public PostReloadDbSessionEvent(ReloadReason r, SimpleSessionImpl session) {
		super(r, session);
	}

}
