package io.neocore.api.event.database;

import java.util.UUID;

import io.neocore.api.event.Raisable;

@Raisable
public class PostUnloadDbSessionEvent extends UuidSupportedDatabaseEvent<UnloadReason> {

	public PostUnloadDbSessionEvent(UnloadReason r, UUID uuid) {
		super(r, uuid);
	}

}
