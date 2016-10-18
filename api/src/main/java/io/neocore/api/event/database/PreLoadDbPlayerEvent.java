package io.neocore.api.event.database;

import java.util.UUID;

import io.neocore.api.event.Raisable;

@Raisable
public class PreLoadDbPlayerEvent extends UuidSupportedDatabaseEvent<LoadReason> {

	public PreLoadDbPlayerEvent(LoadReason r, UUID uuid) {
		super(r, uuid);
	}

}
