package io.neocore.api.database.player;

import java.util.UUID;

import io.neocore.api.database.DatabaseServiceProvider;
import io.neocore.api.player.IdentityProvider;

public interface PlayerService extends DatabaseServiceProvider, IdentityProvider<DatabasePlayer> {
	
	/**
	 * @param id The unique UUID of the player being requested.
	 * @return An instance of NeoPlayer representing the player in question.
	 */
	public DatabasePlayer getPlayer(UUID id);
	
}
