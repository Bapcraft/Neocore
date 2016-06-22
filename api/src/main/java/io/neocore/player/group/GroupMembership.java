package io.neocore.player.group;

import java.time.Instant;
import java.util.Date;

public class GroupMembership {
	
	private Group group;
	private Date start, expires;
	
	public GroupMembership(Group g, Date start, Date expires) {
		
		this.group = g;
		this.start = start;
		this.expires = expires;
		
	}
	
	public GroupMembership(Group g) {
		this(g, Date.from(Instant.MIN), Date.from(Instant.MAX));
	}
	
	public Group getGroup() {
		return this.group;
	}
	
	public Date getBegin() {
		return this.start;
	}
	
	public Date getExpiration() {
		return this.expires;
	}
	
	public boolean isCurrentlyValid() {
		
		Date current = Date.from(Instant.now());
		return this.start.before(current) && this.expires.after(current);
		
	}
	
}
