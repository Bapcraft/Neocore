package io.neocore.api.event.database;

import java.util.UUID;

import io.neocore.api.event.Raisable;

@Raisable
public class PreReloadDbPlayerEvent extends UuidSupportedDatabaseEvent<ReloadReason> {

	public PreReloadDbPlayerEvent(ReloadReason r, UUID uuid) {
		super(r, uuid);
	}

}
