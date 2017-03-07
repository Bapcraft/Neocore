package io.neocore.api.event.database;

import java.util.UUID;

public class UuidSupportedDatabaseEvent<R extends DatabaseInteractionReason> extends DatabaseIdentityEvent<R> {

	private UUID uuid;

	protected UuidSupportedDatabaseEvent(R r, UUID uuid) {

		super(r);

		this.uuid = uuid;

	}

	@Override
	public UUID getUniqueId() {
		return this.uuid;
	}

}
