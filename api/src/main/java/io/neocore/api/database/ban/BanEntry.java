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
	
	public UUID getUniqueId() {
		return this.bannedUuid;
	}
	
	public Date getDateIssued() {
		return this.issued;
	}
	
	public Date getDateExpires() {
		return this.expiration;
	}
	
	public boolean isActive() {
		
		Date now = Date.from(Instant.now());
		
		if (this.isPermanent()) return now.compareTo(this.getDateIssued()) > 0;
		return (now.compareTo(this.getDateIssued()) > 0) && (now.compareTo(this.getDateExpires()) < 0);
		
	}
	
	public boolean isPermanent() {
		return this.expiration == null;
	}
	
	public Context getContext() {
		return this.context;
	}
	
	public boolean isGlobal() {
		return this.getContext() == null;
	}
	
	public String getReason() {
		return this.reason;
	}
	
}
