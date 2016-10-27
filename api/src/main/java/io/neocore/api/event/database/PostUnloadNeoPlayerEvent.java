package io.neocore.api.event.database;

import java.util.UUID;

import io.neocore.api.event.Raisable;

@Raisable
public class PostUnloadNeoPlayerEvent extends UuidSupportedDatabaseEvent<UnloadReason> {

	public PostUnloadNeoPlayerEvent(UnloadReason r, UUID uuid) {
		super(r, uuid);
	}

}
