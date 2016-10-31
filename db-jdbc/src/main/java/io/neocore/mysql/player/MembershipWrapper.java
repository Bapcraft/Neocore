package io.neocore.mysql.player;

import java.util.Date;

import io.neocore.api.player.group.Group;
import io.neocore.api.player.group.GroupMembership;

public class MembershipWrapper extends GroupMembership {
	
	private MysqlGroupMembership wrapper;
	
	public MembershipWrapper(MysqlGroupMembership mgm) {
		
		super(mgm.begin, mgm.end);
		
		this.wrapper = mgm;
		
	}
	
	@Override
	public Group getGroup() {
		return null; // TODO
	}

	@Override
	public void setGroup(Group group) {
		
		this.wrapper.groupName = group.getName();
		this.dirty();
		
	}
	
	@Override
	public void setBegin(Date begin) {
		
		super.setBegin(begin);
		this.wrapper.begin = begin;
		this.dirty();
		
	}

	@Override
	public void setExpiration(Date expiration) {
		
		super.setExpiration(expiration);
		this.wrapper.end = expiration;
		this.dirty();
		
	}

	@Override
	public void setDirty(boolean val) {
		this.wrapper.setDirty(val);
	}

	@Override
	public boolean isDirty() {
		return this.wrapper.isDirty();
	}

	@Override
	public boolean isGloballyValid() {
		return this.wrapper.isGloballyValid();
	}

	
}
