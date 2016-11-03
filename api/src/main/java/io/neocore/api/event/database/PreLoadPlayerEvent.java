package io.neocore.api.event.database;

import java.util.UUID;

import io.neocore.api.event.Raisable;

@Raisable
public class PreLoadPlayerEvent extends UuidSupportedDatabaseEvent<LoadReason> {

	public PreLoadPlayerEvent(LoadReason r, UUID uuid) {
		super(r, uuid);
	}

}
