package io.neocore.api.event.database;

import io.neocore.api.database.session.Session;
import io.neocore.api.event.Raisable;

@Raisable
public class PreFlushDbSessionEvent extends SessionSupportedDatabaseEvent<FlushReason> {

	public PreFlushDbSessionEvent(FlushReason r, Session session) {
		super(r, session);
	}

}
