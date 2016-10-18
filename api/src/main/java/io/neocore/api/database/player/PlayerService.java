package io.neocore.api.database.player;

import java.util.UUID;

import io.neocore.api.database.DatabaseServiceProvider;
import io.neocore.api.database.IdentityLinkage;
import io.neocore.api.database.LoadAsync;

@LoadAsync
public interface PlayerService extends DatabaseServiceProvider, IdentityLinkage<DatabasePlayer> {
	
	/**
	 * @param id The UUID of the player who's last known username we're looking up.
	 * @return The player's last known username, of <code>null</code> if unknown.
	 */
	public String getLastUsername(UUID id);
	
}
