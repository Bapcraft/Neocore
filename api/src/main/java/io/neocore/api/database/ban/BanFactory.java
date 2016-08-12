package io.neocore.api.database.ban;

import java.time.Instant;
import java.util.Date;

import io.neocore.api.player.PlayerIdentity;

public class BanFactory {
	
	public static BanEntry newPermanentBan(PlayerIdentity banned, BanIssuer issuer, String reason) {
		
		BanEntry ban = new BanEntry(banned.getUniqueId(), null);
		
		// Expiration information
		ban.issued = Date.from(Instant.now());
		ban.expiration = null;
		
		// Logical information
		ban.issuer = issuer;
		ban.reason = reason;
		
		return ban;
		
	}
	
	public static BanEntry newTimedBan(PlayerIdentity banned, BanIssuer issuer, String reason, long duration) {
		
		BanEntry ban = newPermanentBan(banned, issuer, reason);
		
		// Adjust expiration
		ban.expiration = Date.from(ban.issued.toInstant().plusMillis(duration));
		
		return ban;
		
	}
	
}
