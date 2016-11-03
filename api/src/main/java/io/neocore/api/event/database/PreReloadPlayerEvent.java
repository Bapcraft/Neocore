package io.neocore.api.event.database;

import java.util.UUID;

import io.neocore.api.event.Raisable;

@Raisable
public class PreReloadPlayerEvent extends UuidSupportedDatabaseEvent<ReloadReason> {

	public PreReloadPlayerEvent(ReloadReason r, UUID uuid) {
		super(r, uuid);
	}

}
