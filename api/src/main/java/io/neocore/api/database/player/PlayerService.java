package io.neocore.api.database.player;

import java.util.UUID;

import io.neocore.api.database.DatabaseServiceProvider;

public interface PlayerService extends DatabaseServiceProvider {
	
	/**
	 * @param id The unique UUID of the player being requested.
	 * @return An instance of NeoPlayer representing the player in question.
	 */
	public DatabasePlayer getPlayer(UUID id);
	
}
