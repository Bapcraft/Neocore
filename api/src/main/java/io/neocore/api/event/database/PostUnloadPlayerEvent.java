package io.neocore.api.event.database;

import java.util.UUID;

import io.neocore.api.event.Raisable;

@Raisable
public class PostUnloadPlayerEvent extends UuidSupportedDatabaseEvent<UnloadReason> {

	public PostUnloadPlayerEvent(UnloadReason r, UUID uuid) {
		super(r, uuid);
	}

}
