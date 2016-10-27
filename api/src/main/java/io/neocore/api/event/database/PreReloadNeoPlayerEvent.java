package io.neocore.api.event.database;

import java.util.UUID;

import io.neocore.api.event.Raisable;

@Raisable
public class PreReloadNeoPlayerEvent extends UuidSupportedDatabaseEvent<ReloadReason> {

	public PreReloadNeoPlayerEvent(ReloadReason r, UUID uuid) {
		super(r, uuid);
	}

}
