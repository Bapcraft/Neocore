package io.neocore.jdbc.player;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import io.neocore.api.NeocoreAPI;
import io.neocore.api.database.AbstractPersistentRecord;
import io.neocore.api.player.group.Group;
import io.neocore.api.player.group.GroupMembership;

@DatabaseTable(tableName = "groupMemberships")
public class JdbcGroupMembership extends AbstractPersistentRecord implements GroupMembership {
	
	@DatabaseField(generatedId = true, allowGeneratedIdInsert = true)
	private int membershipId;
	
	@DatabaseField(canBeNull = false, foreign = true)
	protected JdbcDbPlayer owner;
	
	@DatabaseField(canBeNull = false)
	protected String groupName;
	
	@DatabaseField(canBeNull = true)
	protected Date begin;
	
	@DatabaseField(canBeNull = true)
	protected Date end;
	
	public JdbcGroupMembership() {
		// ORMLite.
	}
	
	public JdbcGroupMembership(JdbcDbPlayer owner, Group g) {
		
		this.owner = owner;
		this.groupName = g.getName();
		
	}

	@Override
	public void setGroup(Group group) {
		
		this.groupName = group.getName();
		this.dirty();
		
	}

	@Override
	public Group getGroup() {
		return NeocoreAPI.getAgent().getPermissionManager().getGroup(this.groupName); // FIXME Breaks encapsulation.
	}

	@Override
	public void setStartDate(Date date) {
		
		this.begin = date;
		this.dirty();
		
	}

	@Override
	public Date getStartDate() {
		return this.begin;
	}

	@Override
	public void setEndDate(Date date) {
		
		this.end = date;
		this.dirty();
		
	}

	@Override
	public Date getEndDate() {
		return this.end;
	}
	
}
