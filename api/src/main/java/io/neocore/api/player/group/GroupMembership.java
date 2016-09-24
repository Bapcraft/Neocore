package io.neocore.api.player.group;

import java.time.Instant;
import java.util.Date;

/**
 * A membership in a group, agnostic of which player it belongs to.
 * 
 * @author treyzania
 */
public abstract class GroupMembership {
	
	private Date start, expires;
	
	public GroupMembership(Date start, Date expires) {
		
		this.start = start;
		this.expires = expires;
		
	}
	
	public GroupMembership() {
		this(Date.from(Instant.MIN), Date.from(Instant.MAX));
	}
	
	/**
	 * @return The group that the membership deals with.
	 */
	public abstract Group getGroup();
	
	/**
	 * @return Gets the date that the membership period begins.
	 */
	public Date getBegin() {
		return this.start;
	}
	
	/**
	 * @return Gets the date that the membership period ends.
	 */
	public Date getExpiration() {
		return this.expires;
	}
	
	/**
	 * @return <code>true</code> if the membership is currently within the before and end times, <code>false</code> otherwise.
	 */
	public boolean isCurrentlyValid() {
		
		Date current = Date.from(Instant.now());
		return this.start.before(current) && this.expires.after(current);
		
	}
	
}
