package io.neocore.api.player.group;

import java.time.Instant;
import java.util.Date;

import io.neocore.api.database.AbstractPersistentRecord;

/**
 * A membership in a group, agnostic of which player it belongs to.
 * 
 * @author treyzania
 */
public abstract class GroupMembership extends AbstractPersistentRecord {
	
	private Date start, expires;
	
	public GroupMembership(Date start, Date expires) {
		
		this.start = start;
		this.expires = expires;
		
	}
	
	public GroupMembership() {
		this(Date.from(Instant.MIN), Date.from(Instant.MAX));
	}
	
	/**
	 * Sets the group that this belongs to.
	 */
	public abstract void setGroup(Group group);
	
	/**
	 * @return The group that the membership deals with.
	 */
	public abstract Group getGroup();
	
	/**
	 * Sets the begin date of the membership.
	 * 
	 * @param begin The begin date.
	 */
	public void setBegin(Date begin) {
		
		this.start = begin;
		this.dirty();
		
	}
	
	/**
	 * @return Gets the date that the membership period begins.
	 */
	public Date getBegin() {
		return this.start;
	}

	/**
	 * Sets the expiration date of the membership.
	 * 
	 * @param expiration The expiration date.
	 */
	public void setExpiration(Date expiration) {
		
		this.expires = expiration;
		this.dirty();
		
	}
	
	/**
	 * @return The date that the membership period ends.
	 */
	public Date getExpiration() {
		return this.expires;
	}
	
	/**
	 * @return <code>true</code> if the membership is currently within the before and end times, <code>false</code> otherwise.
	 */
	public boolean isCurrentlyValid() {
		
		Date current = Date.from(Instant.now());
		return (this.start == null || this.start.before(current)) && (this.expires == null || this.expires.after(current));
		
	}
	
}
