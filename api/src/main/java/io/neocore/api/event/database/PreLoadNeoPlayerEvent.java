package io.neocore.api.event.database;

import java.util.UUID;

import io.neocore.api.event.Raisable;

@Raisable
public class PreLoadNeoPlayerEvent extends UuidSupportedDatabaseEvent<LoadReason> {

	public PreLoadNeoPlayerEvent(LoadReason r, UUID uuid) {
		super(r, uuid);
	}

}
