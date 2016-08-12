package io.neocore.api.database.ban;

import java.util.UUID;

import io.neocore.api.NeocoreAPI;
import io.neocore.api.database.player.PlayerService;
import io.neocore.api.player.PlayerIdentity;

public class PlayerBanIssuer extends BanIssuer {
	
	private UUID uuid;
	
	public PlayerBanIssuer(UUID uuid) {
		this.uuid = uuid;
	}
	
	public PlayerBanIssuer(PlayerIdentity pi) {
		this(pi.getUniqueId());
	}
	
	@Override
	public String getDisplayName() {
		return NeocoreAPI.getAgent().getServiceManager().getService(PlayerService.class).getLastUsername(this.uuid);
	}

}
