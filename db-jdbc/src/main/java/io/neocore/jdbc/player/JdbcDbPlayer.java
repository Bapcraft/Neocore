package io.neocore.jdbc.player;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import io.neocore.api.NeocoreAPI;
import io.neocore.api.database.AbstractPersistentRecord;
import io.neocore.api.database.player.DatabasePlayer;
import io.neocore.api.eco.Account;
import io.neocore.api.player.extension.Extension;
import io.neocore.api.player.group.Group;
import io.neocore.api.player.group.GroupMembership;

@DatabaseTable(tableName = "players")
public class JdbcDbPlayer extends AbstractPersistentRecord implements DatabasePlayer, Comparable<DatabasePlayer> {
	
	@DatabaseField(id = true)
	private UUID uuid;
	
	@DatabaseField(canBeNull = false)
	private String lastUsername = "[undefined]";
	
	@ForeignCollectionField
	private ForeignCollection<JdbcPlayerAccount> accounts;
	
	@ForeignCollectionField
	private ForeignCollection<JdbcGroupMembership> groupMemberships;
	
	@ForeignCollectionField
	private ForeignCollection<JdbcExtensionRecord> extensions;
	
	@DatabaseField(canBeNull = false)
	private Date firstLogin = new Date();
	
	@DatabaseField(canBeNull = false)
	private Date lastLogin = new Date();
	
	@DatabaseField
	private long loginCount;
	
	public JdbcDbPlayer() {
		// ORMLite.
	}
	
	public JdbcDbPlayer(UUID uuid) {
		this.uuid = uuid;
	}
	
	@Override
	public UUID getUniqueId() {
		return this.uuid;
	}
	
	@Override
	public List<GroupMembership> getGroupMemberships() {
		return new ArrayList<>(this.groupMemberships);
	}
	
	@Override
	public void addGroupMembership(GroupMembership group) {
		
		if (group instanceof JdbcGroupMembership) {
			
			this.groupMemberships.add((JdbcGroupMembership) group);
			this.dirty();
			
		} else {
			NeocoreAPI.getLogger().severe("Someone gave me a group membership that isn't one I like.");
		}
		
	}
	
	@Override
	public GroupMembership addGroup(Group group) {
		return null; // FIXME TODO
	}
	
	@Override
	public void removeGroupMembership(GroupMembership gm) {
		
		Iterator<JdbcGroupMembership> iter = this.groupMemberships.iterator();
		while (iter.hasNext()) {
			
			JdbcGroupMembership mgm = iter.next();
			if (mgm == gm) iter.remove();
			
		}
		
	}
	
	@Override
	public Account getAccount(String currency) {
		
		for (JdbcPlayerAccount acct : this.accounts) {
			if (acct.currency.equals(currency)) return acct;
		}
		
		return null;
		
	}
	
	@Override
	public Extension getExtension(String name) {
		
		for (JdbcExtensionRecord ext : this.extensions) {
			if (ext.type.equals(name)) return ext.deserialize();
		}
		
		return null;
		
	}
	
	@Override
	public void putExtension(Extension ext) {
		
		for (JdbcExtensionRecord myExt : this.extensions) {
			
			if (myExt.type.equals(ext.getName())) {
				
				myExt.data = ext.serialize();
				this.dirty();
				break;
				
			}
			
		}
		
		// At this point we're sure it's not in it so we can add it.
		this.extensions.add(new JdbcExtensionRecord(ext));
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

	@Override
	public int compareTo(DatabasePlayer o) {
		return this.getUniqueId().compareTo(o.getUniqueId());
	}

	@Override
	public void setFirstLogin(Date date) {
		
		this.firstLogin = date;
		this.dirty();
		
	}

	@Override
	public Date getFirstLogin() {
		return this.firstLogin;
	}

	@Override
	public void setLastLogin(Date date) {
		
		this.lastLogin = date;
		this.dirty();
		
	}

	@Override
	public Date getLastLogin() {
		return this.lastLogin;
	}

	@Override
	public void setLoginCount(int count) {
		
		this.loginCount = count;
		this.dirty();
		
	}

	@Override
	public int getLoginCount() {
		return (int) this.loginCount;
	}
	
}
