package io.neocore.api.event.database;

import io.neocore.api.database.session.Session;
import io.neocore.api.event.Raisable;

@Raisable
public class PostFlushDbSessionEvent extends SessionSupportedDatabaseEvent<FlushReason> {

	public PostFlushDbSessionEvent(FlushReason r, Session session) {
		super(r, session);
	}

}
