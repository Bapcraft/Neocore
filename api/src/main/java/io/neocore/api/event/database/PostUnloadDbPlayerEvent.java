package io.neocore.api.event.database;

import java.util.UUID;

import io.neocore.api.event.Raisable;

@Raisable
public class PostUnloadDbPlayerEvent extends UuidSupportedDatabaseEvent<UnloadReason> {

	public PostUnloadDbPlayerEvent(UnloadReason r, UUID uuid) {
		super(r, uuid);
	}

}
