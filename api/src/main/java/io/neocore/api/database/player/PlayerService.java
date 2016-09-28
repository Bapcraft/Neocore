package io.neocore.api.database.player;

import java.util.UUID;

import io.neocore.api.database.DatabaseServiceProvider;
import io.neocore.api.player.IdentityProvider;

public interface PlayerService extends DatabaseServiceProvider, IdentityProvider<DatabasePlayer> {
	
	/**
	 * @param id The UUID of the player who's last known username we're looking up.
	 * @return The player's last known username, of <code>null</code> if unknown.
	 */
	public String getLastUsername(UUID id);
	
	/**
	 * Updates the player data in the cache with the current version of the
	 * data in the database.
	 * 
	 * @param player The player to reload.
	 */
	public void reload(DatabasePlayer player);
	
	/**
	 * Asynchronously reloads the player data.
	 * 
	 * @param player The player to reload.
	 * @param callback The callback to call once completed.
	 */
	public default void reload(DatabasePlayer player, Runnable callback) {
		
		this.reload(player);
		callback.run();
		
	}
	
	/**
	 * Flushes the player data to the database, does not unload the player.
	 * 
	 * @param player The player to flush.
	 */
	public void flush(DatabasePlayer player);
	
	/**
	 * Asynchronously flushed the player data to the database.
	 * 
	 * @param player The player to flush.
	 * @param callback The callback to call once completed.
	 */
	public default void flush(DatabasePlayer player, Runnable callback) {
		
		this.flush(player);
		callback.run();
		
	}
	
}
