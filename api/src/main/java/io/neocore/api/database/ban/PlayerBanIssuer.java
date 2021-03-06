package io.neocore.api.database.ban;

import java.io.IOException;
import java.util.UUID;

import io.neocore.api.NeocoreAPI;
import io.neocore.api.database.player.PlayerService;
import io.neocore.api.player.PlayerIdentity;

/**
 * A ban issuer that returns the last-known username for the given UUID.
 * 
 * @author treyzania
 */
public class PlayerBanIssuer extends BanIssuer {

	protected UUID uuid;

	public PlayerBanIssuer(UUID uuid) {
		this.uuid = uuid;
	}

	public PlayerBanIssuer(PlayerIdentity pi) {
		this(pi.getUniqueId());
	}

	@Override
	public String getDisplayName() {

		// FIXME Breaks encapsulation?
		try {
			return NeocoreAPI.getAgent().getServiceManager().getService(PlayerService.class).getLastUsername(this.uuid);
		} catch (IOException e) {
			return "[unresolvable]";
		}

	}

}
