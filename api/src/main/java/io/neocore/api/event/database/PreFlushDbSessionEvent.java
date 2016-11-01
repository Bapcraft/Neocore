package io.neocore.api.event.database;

import io.neocore.api.database.session.SimpleSessionImpl;
import io.neocore.api.event.Raisable;

@Raisable
public class PreFlushDbSessionEvent extends SessionSupportedDatabaseEvent<FlushReason> {

	public PreFlushDbSessionEvent(FlushReason r, SimpleSessionImpl session) {
		super(r, session);
	}

}
