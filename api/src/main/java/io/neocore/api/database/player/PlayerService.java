package io.neocore.api.database.player;

import java.util.UUID;

import io.neocore.api.LoadAsync;
import io.neocore.api.database.DatabaseServiceProvider;
import io.neocore.api.database.IdentityLinkage;
import io.neocore.api.player.PlayerIdentity;

@LoadAsync
public interface PlayerService extends DatabaseServiceProvider, IdentityLinkage<DatabasePlayer> {
	
	/**
	 * @param id The UUID of the player who's last known username we're looking up.
	 * @return The player's last known username, of <code>null</code> if unknown.
	 */
	public String getLastUsername(UUID id);

	@Override
	public default Class<? extends PlayerIdentity> getIdentityClass() {
		return DatabasePlayer.class;
	}
	
}
