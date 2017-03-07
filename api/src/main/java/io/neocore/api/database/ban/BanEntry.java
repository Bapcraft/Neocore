package io.neocore.api.database.ban;

import java.util.Date;
import java.util.UUID;

import io.neocore.api.host.Context;

public interface BanEntry {

	/**
	 * Sets the UUID for this bn.
	 */
	public void setPlayerId(UUID playerId);

	/**
	 * @return The UUID of the banned player.
	 */
	public UUID getPlayerId();

	/**
	 * @param date
	 *            The date issued.
	 */
	public void setDateIssued(Date date);

	/**
	 * @return The date the ban was issued.
	 */
	public Date getDateIssued();

	/**
	 * @param date
	 *            The date the ban goes into effect.
	 */
	public void setStartDate(Date date);

	/**
	 * @return The date the ban goes info effect.
	 */
	public Date getStartDate();

	/**
	 * @param date
	 *            The date the ban expires.
	 */
	public void setExpirationDate(Date date);

	/**
	 * @return The date at which the ban will no longer count.
	 */
	public Date getExpirationDate();

	/**
	 * @return <code>true</code> if the current time is between the bounds for
	 *         this ban.
	 */
	public default boolean isActive() {

		Date now = new Date();
		return (this.getDateIssued() == null || this.getDateIssued().before(now))
				&& (this.getExpirationDate() == null || this.getExpirationDate().after(now));

	}

	/**
	 * @return <code>true</code> if the ban has no end date.
	 */
	public default boolean isPermanent() {
		return this.getExpirationDate() == null;
	}

	/**
	 * Sets the issuer ID. If the issuer isn't a player, then it should be a
	 * version 1 UUID with all 0s. This behavior will be changing soon.
	 * 
	 * @param issuerId
	 *            The UUID of the issuer.
	 */
	public void setIssuerId(UUID issuerId);

	/**
	 * Returns the UUID of the issuer. If the issuer is not a player, then
	 * returns a version 1 UUID with all 0s.
	 * 
	 * @return The UUID of the issuer.
	 */
	public UUID getIssuerId();

	/**
	 * @param reason
	 *            The reason for the ban, to be displayed to users.
	 */
	public void setReason(String reason);

	/**
	 * @return The reason for the ban, to be displayed to users.
	 */
	public String getReason();

	/**
	 * Sets the context for this ban. Only the short name of the context will be
	 * stored.
	 * 
	 * @param context
	 *            The context for the ban.
	 */
	public void setContext(Context context);

	/**
	 * @return The context in which this ban is active. <code>null</code> if
	 *         valid everywhere.
	 */
	public Context getContext();

	/**
	 * @return If the context is <code>null</code>, therefore the ban is active
	 *         everywhere.
	 */
	public default boolean isGlobal() {
		return this.getContext() == null;
	}

}
