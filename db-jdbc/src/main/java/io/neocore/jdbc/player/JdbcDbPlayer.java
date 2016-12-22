package io.neocore.jdbc.player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import io.neocore.api.NeocoreAPI;
import io.neocore.api.database.AbstractPersistentRecord;
import io.neocore.api.database.player.DatabasePlayer;
import io.neocore.api.eco.Account;
import io.neocore.api.player.extension.Extension;
import io.neocore.api.player.group.Flair;
import io.neocore.api.player.group.Group;
import io.neocore.api.player.group.GroupMembership;
import io.neocore.jdbc.group.JdbcFlair;

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
	
	@DatabaseField
	private int restrictionLevel;
	
	@DatabaseField
	private String currentFlairPrefix;
	
	@DatabaseField
	private String currentFlairSuffix;
	
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
	public List<Account> getAccounts() {
		
		List<Account> out = new ArrayList<>();
		CloseableIterator<JdbcPlayerAccount> iter = this.accounts.closeableIterator();
		while (iter.hasNext()) {
			out.add(iter.next());
		}
		
		iter.closeQuietly();
		return Collections.unmodifiableList(out);
		
	}
	
	@Override
	public void putExtension(Extension ext) {
		
		// Update the existing one.
		CloseableIterator<JdbcExtensionRecord> iter = this.extensions.closeableIterator();
		while (iter.hasNext()) {
			
			JdbcExtensionRecord test = iter.next();
			if (test.type.equals(ext.getName())) {
				
				// Update the data.
				test.data = ext.serialize();
				
				// And close out everything.
				this.dirty();
				iter.closeQuietly();
				return;
				
			}

		}
		
		// At this point we're sure it's not in it so far so we can add it.
		this.extensions.add(new JdbcExtensionRecord(ext));
		this.dirty();
		
	}
	
	@Override
	public Extension getExtension(String name) {
		
		for (JdbcExtensionRecord ext : this.extensions) {
			if (ext.type.equals(name)) return ext.deserialize();
		}
		
		return null;
		
	}
	
	@Override
	public List<String> getExtensionNames() {
		
		List<String> names = new ArrayList<>();
		CloseableIterator<JdbcExtensionRecord> iter = this.extensions.closeableIterator();
		while (iter.hasNext()) {
			names.add(iter.next().type);
		}
		
		iter.closeQuietly();
		return names;
		
	}
	
	@Override
	public List<Extension> getExtensions() {
		
		List<Extension> names = new ArrayList<>();
		CloseableIterator<JdbcExtensionRecord> iter = this.extensions.closeableIterator();
		while (iter.hasNext()) {
			names.add(iter.next().deserialize());
		}
		
		iter.closeQuietly();
		return names;
		
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
	
	@Override
	public void setRestrictionLevel(int level) {
		this.restrictionLevel = level;
	}
	
	@Override
	public int getRestrictionLevel() {
		return this.restrictionLevel;
	}

	@Override
	public void setCurrentFlair(Flair flair) {
		
		this.currentFlairPrefix = flair.getPrefix();
		this.currentFlairSuffix = flair.getSuffix();
		
	}

	@Override
	public Flair getCurrentFlair() {
		return new JdbcFlair(this.currentFlairPrefix, this.currentFlairSuffix); // Probably shouldn't be instantiating it like that.
	}
	
}
