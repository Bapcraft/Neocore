package io.neocore.api.event.database;

import java.util.UUID;

import io.neocore.api.database.player.DatabasePlayer;

public class PlayerSupportedDatabaseEvent<R extends DatabaseInteractionReason> extends DatabaseIdentityEvent<R> {
	
	private DatabasePlayer player;
	
	protected PlayerSupportedDatabaseEvent(R r, DatabasePlayer player) {
		super(r);
	}

	@Override
	public UUID getUniqueId() {
		return this.getPlayer().getUniqueId();
	}
	
	public DatabasePlayer getPlayer() {
		return this.player;
	}
	
}
