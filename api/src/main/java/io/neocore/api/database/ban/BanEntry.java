package io.neocore.api.database.ban;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import io.neocore.api.host.Context;

public class BanEntry {
	
	private final UUID bannedUuid;
	protected Date issued, expiration;
	protected Context context;
	
	protected BanIssuer issuer;
	protected String reason;
	
	protected BanEntry(UUID banned, Context context) {	
		
		this.bannedUuid = banned;
		this.context = context;
		
	}
	
	/**
	 * @return The UUID of the banned player.
	 */
	public UUID getUniqueId() {
		return this.bannedUuid;
	}
	
	/**
	 * @return The date that the ban goes into effect, also usually the date that the ban was issued.
	 */
	public Date getDateIssued() {
		return this.issued;
	}
	
	/**
	 * @return The date that the ban expires on, <code>null</code> if permanent.
	 */
	public Date getDateExpires() {
		return this.expiration;
	}
	
	/**
	 * @return If the ban is currently active at the time of invocation.
	 */
	public boolean isActive() {
		
		Date now = Date.from(Instant.now());
		
		if (this.isPermanent()) return now.compareTo(this.getDateIssued()) > 0;
		return (now.compareTo(this.getDateIssued()) > 0) && (now.compareTo(this.getDateExpires()) < 0);
		
	}
	
	/**
	 * @return If the ban is permanent.
	 */
	public boolean isPermanent() {
		return this.expiration == null;
	}
	
	/**
	 * @return The context that the ban is applied for, <code>null</code> if global.
	 */
	public Context getContext() {
		return this.context;
	}
	
	/**
	 * @return <code>true</code> if this is a global ban.
	 */
	public boolean isGlobal() {
		return this.getContext() == null;
	}
	
	/**
	 * @return The "thing" that issued this ban.
	 */
	public BanIssuer getIssuer() {
		return this.issuer;
	}
	
	/**
	 * @return The reason for the ban.
	 */
	public String getReason() {
		return this.reason;
	}
	
	public void save() {
		// TODO
	}
	
}
