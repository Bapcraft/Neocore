package io.neocore.api.database.ban;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import io.neocore.api.host.Context;
import io.neocore.api.player.PlayerIdentity;

public class BanFactory {
	
	/**
	 * Creates a new permanent ban.
	 * 
	 * @param banned The player to be banned.
	 * @param issuer The "thing" that issued the ban.
	 * @param reason The reason for the ban.
	 * @return The ban entry.
	 */
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
	
	/**
	 * Creates a temporary ban.
	 * 
	 * @param banned The player to be banned.
	 * @param issuer The "thing" that issued the ban.
	 * @param reason The reason for the ban.
	 * @param duration The duration, in milliseconds, of the ban.
	 * @return The ban entry.
	 */
	public static BanEntry newTimedBan(PlayerIdentity banned, BanIssuer issuer, String reason, long duration) {
		
		BanEntry ban = newPermanentBan(banned, issuer, reason);
		
		// Adjust expiration
		ban.expiration = Date.from(ban.issued.toInstant().plusMillis(duration));
		
		return ban;
		
	}
	
	/**
	 * Creates a new ban with the exact parameters specified.  No guarantee that it will go into effect immediately.
	 * 
	 * @param uuid The UUID of the player banned.
	 * @param ctx The context of the ban.
	 * @param issuer The "thing" that issued the ban.
	 * @param reason The reason for the ban.
	 * @param start The start <code>Date</code> of the ban.
	 * @param end The end <code>Date</code> of the ban.
	 * @return The explicitly-defined ban entry.
	 */
	public static BanEntry newExplicitBan(UUID uuid, Context ctx, BanIssuer issuer, String reason, Date start, Date end) {
		
		BanEntry ban = new BanEntry(uuid, ctx);
		
		ban.issuer = issuer;
		ban.reason = reason;
		
		ban.issued = start;
		ban.expiration = end;
		
		return ban;
		
	}
	
}
