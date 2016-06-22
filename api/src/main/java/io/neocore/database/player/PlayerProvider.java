package io.neocore.database.player;

import java.util.UUID;

import io.neocore.database.DatabaseServiceProvider;

public interface PlayerProvider extends DatabaseServiceProvider {
	
	/**
	 * @param id The unique UUID of the player being requested.
	 * @return An instance of NeoPlayer representing the player in question.
	 */
	public DatabasePlayer getPlayer(UUID id);
	
}
