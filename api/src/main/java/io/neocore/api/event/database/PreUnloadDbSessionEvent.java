package io.neocore.api.event.database;

import io.neocore.api.database.session.Session;
import io.neocore.api.event.Raisable;

@Raisable
public class PreUnloadDbSessionEvent extends SessionSupportedDatabaseEvent<UnloadReason> {

	public PreUnloadDbSessionEvent(UnloadReason r, Session session) {
		super(r, session);
	}

}
