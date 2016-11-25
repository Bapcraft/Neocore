package io.neocore.jdbc.group;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import io.neocore.api.NeocoreAPI;
import io.neocore.api.database.AbstractPersistentRecord;
import io.neocore.api.host.Context;
import io.neocore.api.player.group.Flair;
import io.neocore.api.player.group.Group;
import io.neocore.api.player.group.PermissionEntry;

@DatabaseTable(tableName = "groups")
public class JdbcGroup extends AbstractPersistentRecord implements Group {
	
	@DatabaseField(id = true)
	private UUID groupId;
	
	@DatabaseField(canBeNull = false)
	private String name = "[undefined]";
	
	@DatabaseField(canBeNull = false)
	private String prettyName = "[UNDEFINED]";
	
	@DatabaseField
	private String parentName;
	
	@ForeignCollectionField
	private ForeignCollection<JdbcFlair> flairs;
	
	@ForeignCollectionField
	private ForeignCollection<JdbcPermissionEntry> perms;
	
	@DatabaseField
	private int priority = 0;
	
	@DatabaseField
	private int restrictionLevel = 0;
	
	// This is for parental resolution.
	protected transient JdbcGroupService resolver;
	
	public JdbcGroup() {
		this.groupId = UUID.randomUUID();
	}
	
	public JdbcGroup(String name) {
		
		this.groupId = UUID.randomUUID();
		this.name = name;
		
		// Capitalize the first letter of the set name for the pretty name.
		this.prettyName = Character.toUpperCase(name.charAt(0)) + name.substring(1);
		
	}
	
	public UUID getGroupId() {
		return this.groupId;
	}
	
	@Override
	public void setName(String name) {
		
		this.name = name;
		this.dirty();
		
	}
	
	@Override
	public String getName() {
		return this.name;
	}
	
	@Override
	public void setDisplayName(String displayName) {
		
		this.prettyName = displayName;
		this.dirty();
		
	}
	
	@Override
	public String getDisplayName() {
		return this.prettyName;
	}
	
	@Override
	public void setParent(Group parent) {
		
		this.parentName = parent.getName();
		this.dirty();
		
	}
	
	@Override
	public Group getParent() {
		return this.resolver.getGroup(this.parentName);
	}
	
	@Override
	public void addFlair(Flair flair) {
		
		if (flair instanceof JdbcFlair) {
			this.flairs.add((JdbcFlair) flair);
		} else {
			this.flairs.add(new JdbcFlair(flair.getPrefix(), flair.getSuffix())); // Dirty hack.
		}
		
	}
	
	@Override
	public boolean removeFlair(Flair flair) {
		
		boolean did = false;
		CloseableIterator<JdbcFlair> iter = null;
		try {
			
			iter = this.flairs.closeableIterator();
			while (iter.hasNext()) {
				
				JdbcFlair colFlair = iter.next();
				if (colFlair.getPrefix().equals(flair.getPrefix()) && colFlair.getSuffix().equals(flair.getSuffix())) {
					
					iter.remove();
					did = true;
					
				}
				
			}
			
		} catch (Exception e) {
			NeocoreAPI.getLogger().log(Level.WARNING, "Problem removing flair.", e);
		}
		
		// Close it if we did anything.
		iter.closeQuietly();
		
		if (did) this.dirty();
		return did;
		
	}
	
	@Override
	public List<Flair> getFlairs() {
		
		List<Flair> out = new ArrayList<>();
		
		CloseableIterator<JdbcFlair> iter = this.flairs.closeableIterator();
		while (iter.hasNext()) {
			out.add(iter.next());
		}
		
		iter.closeQuietly();
		return Collections.unmodifiableList(out);
		
	}
	
	@Override
	public void setPermission(Context context, String node, boolean state) {
		
		this.perms.add(new JdbcPermissionEntry(context, node, state));
		this.dirty();
		
	}

	@Override
	public void unsetPermission(Context context, String node) {
		
		this.perms.removeIf(e -> e.getContext().equals(context) && e.getPermissionNode().equals(node));
		this.dirty();
		
	}
	
	@Override
	public List<PermissionEntry> getPermissions() {
		
		// Stupid crap to make it immutable.
		List<PermissionEntry> out = new ArrayList<>();
		CloseableIterator<JdbcPermissionEntry> iter = this.perms.closeableIterator();
		while (iter.hasNext()) {
			out.add(iter.next());
		}
		
		iter.closeQuietly();
		return Collections.unmodifiableList(out);
		
	}
	
	@Override
	public void setPriority(int priority) {
		
		this.priority = priority;
		this.dirty();
		
	}
	
	@Override
	public int getPriority() {
		return this.priority;
	}
	
	@Override
	public void setRestrictionLevel(int restriction) {
		
		this.restrictionLevel = restriction;
		this.dirty();
		
	}
	
	@Override
	public int getRestrictionLevel() {
		return this.restrictionLevel;
	}
	
}
