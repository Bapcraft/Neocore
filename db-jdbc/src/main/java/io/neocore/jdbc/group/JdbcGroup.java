package io.neocore.jdbc.group;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

	@DatabaseField(generatedId = true, allowGeneratedIdInsert = true)
	private int groupId;

	@DatabaseField(canBeNull = false)
	private String name = "[undefined]";

	@DatabaseField(canBeNull = false)
	private String prettyName = "[UNDEFINED]";

	@DatabaseField
	private String parentName;

	@ForeignCollectionField
	protected ForeignCollection<JdbcFlair> flairs;
	private Set<JdbcFlair> updatedFlairs = new HashSet<>();

	@ForeignCollectionField
	protected ForeignCollection<JdbcPermissionEntry> perms;

	@DatabaseField
	private int priority = 0;

	@DatabaseField
	private int restrictionLevel = 0;

	@DatabaseField
	private boolean secret = false;

	// This is for parental resolution.
	protected transient JdbcGroupService resolver;

	public JdbcGroup() {

	}

	public JdbcGroup(String name) {

		this.name = name;

		// Capitalize the first letter of the set name for the pretty name.
		this.prettyName = Character.toUpperCase(name.charAt(0)) + name.substring(1);

	}

	public int getGroupId() {
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
		return this.parentName != null ? this.resolver.getGroup(this.parentName) : null;
	}

	@Override
	public void addFlair(Flair flair) {

		JdbcFlair f;

		if (flair instanceof JdbcFlair) {
			f = (JdbcFlair) flair;
		} else {
			f = new JdbcFlair(flair.getPrefix(), flair.getSuffix()); // Dirty
																		// hack?
		}

		this.flairs.add(f);
		this.updatedFlairs.add(f);

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

		if (did)
			this.dirty();
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

	private JdbcPermissionEntry getPermEntry(Context context, String node) {

		CloseableIterator<JdbcPermissionEntry> iter = this.perms.closeableIterator();
		while (iter.hasNext()) {

			JdbcPermissionEntry entry = iter.next();
			boolean contextEqual = (entry.getContext() == null && context == null)
					|| (entry.getContext() != null && entry.getContext().getName().equals(context.getName()));
			if (contextEqual && entry.getPermissionNode().equals(node)) {

				iter.closeQuietly();
				return entry;

			}

		}

		iter.closeQuietly();
		return null;

	}

	@Override
	public void setPermission(Context context, String node, boolean state) {

		PermState to = state ? PermState.TRUE : PermState.FALSE;

		JdbcPermissionEntry pe = this.getPermEntry(context, node);
		if (pe == null) {

			pe = new JdbcPermissionEntry(context, node, to);
			this.perms.add(pe);

		} else {

			pe.setState(to);

			try {
				this.perms.update(pe);
			} catch (SQLException e) {
				NeocoreAPI.getLogger().log(Level.SEVERE, "Problem updating permission setting!", e);
			}

		}

		this.dirty();
		this.flush();

	}

	@Override
	public void unsetPermission(Context context, String node) {

		JdbcPermissionEntry pe = this.getPermEntry(context, node);
		if (pe != null) {

			pe.setState(PermState.UNSET);

			try {
				this.perms.update(pe);
			} catch (SQLException e) {
				NeocoreAPI.getLogger().log(Level.SEVERE, "Problem removing permission setting!", e);
			}

			this.dirty();
			this.flush();

		} else {
			NeocoreAPI.getLogger().warning("Tried to unset a permission that was never set.");
		}

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

	@Override
	public void setSecret(boolean secret) {

		this.secret = secret;
		this.dirty();

	}

	@Override
	public boolean isSecret() {
		return this.secret;
	}

	@Override
	public void flush() {

		this.updatedFlairs.forEach(f -> {

			try {
				this.flairs.update(f);
			} catch (SQLException e) {
				NeocoreAPI.getLogger().log(Level.SEVERE, "Problem saving group data.", e);
			}

		});

		this.updatedFlairs.clear();

		super.flush();

	}

}
