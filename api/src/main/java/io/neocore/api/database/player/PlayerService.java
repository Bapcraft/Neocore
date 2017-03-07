package io.neocore.api.database.player;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import io.neocore.api.LoadAsync;
import io.neocore.api.database.DatabaseServiceProvider;
import io.neocore.api.database.IdentityLinkage;
import io.neocore.api.player.PlayerIdentity;

@LoadAsync
public interface PlayerService extends DatabaseServiceProvider, IdentityLinkage<DatabasePlayer> {
	
	/**
	 * @param id The UUID of the player who's last known username we're looking up
	 * @return The player's last known username, of <code>null</code> if unknown
	 * @throws IOException TODO
	 */
	public String getLastUsername(UUID id) throws IOException;
	
	/**
	 * Does a reverse-lookup of a player's UUID from their last-known username.  
	 * 
	 * @param name The player's last-known username
	 * @return The player's UUID
	 * @throws IOException TODO
	 */
	public List<UUID> resolveUUIDs(String name) throws IOException;
	
	@Override
	public default Class<? extends PlayerIdentity> getIdentityClass() {
		return DatabasePlayer.class;
	}
	
}
