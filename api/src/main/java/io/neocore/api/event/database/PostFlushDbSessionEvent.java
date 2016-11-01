package io.neocore.api.event.database;

import io.neocore.api.database.session.SimpleSessionImpl;
import io.neocore.api.event.Raisable;

@Raisable
public class PostFlushDbSessionEvent extends SessionSupportedDatabaseEvent<FlushReason> {

	public PostFlushDbSessionEvent(FlushReason r, SimpleSessionImpl session) {
		super(r, session);
	}

}
