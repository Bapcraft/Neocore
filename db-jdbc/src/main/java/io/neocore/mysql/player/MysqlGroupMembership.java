package io.neocore.mysql.player;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import io.neocore.api.database.AbstractPersistentRecord;
import io.neocore.api.player.group.GroupMembership;

@DatabaseTable(tableName = "groupMemberships")
public class MysqlGroupMembership extends AbstractPersistentRecord {
	
	@DatabaseField(canBeNull = false, foreign = true)
	protected MysqlDbPlayer owner;
	
	@DatabaseField
	protected String groupName;
	
	@DatabaseField(canBeNull = true)
	protected Date begin;
	
	@DatabaseField(canBeNull = true)
	protected Date end;
	
	public MysqlGroupMembership() {
		// ORMLite.
	}
	
	public MysqlGroupMembership(MysqlDbPlayer owner, MembershipWrapper em) {
		
		this.owner = owner;
		
		this.groupName = null;
		this.begin = em.getBegin();
		this.end = em.getExpiration();
		
	}
	
	public GroupMembership wrap() {
		return new MembershipWrapper(this);
	}

	@Override
	public void setDirty(boolean val) {
		this.owner.setDirty(val);
	}

	@Override
	public boolean isDirty() {
		return this.owner.isDirty();
	}

	@Override
	public boolean isGloballyValid() {
		return this.owner.isGloballyValid();
	}

}
