package io.neocore.api.event.database;

import io.neocore.api.database.session.SimpleSessionImpl;
import io.neocore.api.event.Raisable;

@Raisable
public class PreUnloadDbSessionEvent extends SessionSupportedDatabaseEvent<UnloadReason> {

	public PreUnloadDbSessionEvent(UnloadReason r, SimpleSessionImpl session) {
		super(r, session);
	}

}
