package io.neocore.mysql.player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import io.neocore.api.database.AbstractPersistentRecord;
import io.neocore.api.database.player.DatabasePlayer;
import io.neocore.api.eco.Account;
import io.neocore.api.player.extension.Extension;
import io.neocore.api.player.group.Group;
import io.neocore.api.player.group.GroupMembership;

@DatabaseTable(tableName = "players")
public class MysqlDbPlayer extends AbstractPersistentRecord implements DatabasePlayer {
	
	@DatabaseField(id = true)
	private UUID uuid;
	
	@DatabaseField(canBeNull = false)
	private String lastUsername;
	
	@ForeignCollectionField
	private ForeignCollection<MysqlPlayerAccount> accounts;
	
	@ForeignCollectionField
	private ForeignCollection<MysqlGroupMembership> groupMemberships;
	
	@ForeignCollectionField
	private ForeignCollection<MysqlExtensionRecord> extensions;
	
	public MysqlDbPlayer() {
		// ORMLite.
	}
	
	@Override
	public UUID getUniqueId() {
		return this.uuid;
	}
	
	@Override
	public List<GroupMembership> getGroupMemberships() {
		
		// Export the things.
		List<GroupMembership> gms = new ArrayList<>(this.groupMemberships.size());
		this.groupMemberships.forEach(c -> gms.add(c.wrap()));
		return gms;
		
	}
	
	@Override
	public void addGroupMembership(GroupMembership group) {
		
		// TODO Make this not true anymore.
		if (!(group instanceof MembershipWrapper)) throw new IllegalArgumentException("Group needs to be DB-native!");
		
		this.groupMemberships.add(new MysqlGroupMembership(this, (MembershipWrapper) group));
		this.dirty();
		
	}
	
	@Override
	public void removeGroupMembership(Group group) {
		
		Iterator<MysqlGroupMembership> iter = this.groupMemberships.iterator();
		while (iter.hasNext()) {
			
			MysqlGroupMembership mgm = iter.next();
			
			if (mgm.groupName.equals(group.getName())) {
				
				iter.remove();
				this.dirty(); // We can do it multiple times.
				
			}
			
		}
		
	}
	
	@Override
	public Account getAccount(String currency) {
		
		for (MysqlPlayerAccount acct : this.accounts) {
			if (acct.currency.equals(currency)) return acct;
		}
		
		return null;
		
	}
	
	@Override
	public Extension getExtension(String name) {
		
		for (MysqlExtensionRecord ext : this.extensions) {
			if (ext.type.equals(name)) return ext.deserialize();
		}
		
		return null;
		
	}
	
	@Override
	public void putExtension(Extension ext) {
		
		for (MysqlExtensionRecord myExt : this.extensions) {
			
			if (myExt.type.equals(ext.getName())) {
				
				myExt.data = ext.serialize();
				this.dirty();
				break;
				
			}
			
		}
		
		// At this point we're sure it's not in it so we can add it.
		this.extensions.add(new MysqlExtensionRecord(ext));
		this.dirty();
		
	}
	
	@Override
	public void setLastUsername(String name) {
		
		this.lastUsername = name;
		this.dirty();
		
	}
	
	@Override
	public String getLastUsername() {
		return this.lastUsername;
	}
	
}
