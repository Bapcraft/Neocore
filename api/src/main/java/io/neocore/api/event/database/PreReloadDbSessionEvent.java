package io.neocore.api.event.database;

import java.util.UUID;

import io.neocore.api.event.Raisable;

@Raisable
public class PreReloadDbSessionEvent extends UuidSupportedDatabaseEvent<ReloadReason> {

	public PreReloadDbSessionEvent(ReloadReason r, UUID uuid) {
		super(r, uuid);
	}

}
