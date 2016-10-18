package io.neocore.api.event.database;

import java.util.UUID;

import io.neocore.api.event.Raisable;

@Raisable
public class PreLoadDbSessionEvent extends UuidSupportedDatabaseEvent<LoadReason> {

	public PreLoadDbSessionEvent(LoadReason r, UUID uuid) {
		super(r, uuid);
	}

}
