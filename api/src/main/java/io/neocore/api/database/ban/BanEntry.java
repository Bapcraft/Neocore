package io.neocore.api.database.ban;

import java.util.Date;
import java.util.UUID;

import io.neocore.api.host.Context;

public interface BanEntry {
	
	/**
	 * @return The UUID of the banned player.
	 */
	public UUID getPlayerId();
	
	/**
	 * Returns the date that the ban was issued.  Also counts as a "begin"
	 * date for the ban.
	 * 
	 * @return The date the ban was issued.
	 */
	public Date getDateIssued();
	
	/**
	 * @return The date at which the ban will no longer count. 
	 */
	public Date getExpirationDate();
	
	/**
	 * @return <code>true</code> if the current time is between the bounds for this ban.
	 */
	public default boolean isActive() {
		
		Date now = new Date();
		return (this.getDateIssued() == null || this.getDateIssued().before(now)) && (this.getExpirationDate() == null || this.getExpirationDate().after(now));
		
	}
	
	/**
	 * @return <code>true</code> if the ban has no end date.
	 */
	public default boolean isPermanent() {
		return this.getExpirationDate() == null;
	}
	
	/**
	 * Returns the UUID of the issuer.  If the issuer is not a player, then
	 * returns a version 1 UUID with all 0s.
	 * 
	 * @return The UUID of the issuer.
	 */
	public UUID getIssuerId();
	
	/**
	 * @return The reason for the ban, to be displayed to users.
	 */
	public String getReason();
	
	/**
	 * @return The context in which this ban is active.  <code>null</code> if valid everywhere.
	 */
	public Context getContext();
	
	/**
	 * @return If the context is <code>null</code>, therefore the ban is active everywhere.
	 */
	public default boolean isGlobal() {
		return this.getContext() == null;
	}
	
}
