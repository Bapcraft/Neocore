package io.neocore.jdbc.group;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import io.neocore.api.database.AbstractPersistentRecord;
import io.neocore.api.host.Context;
import io.neocore.api.host.LesserContext;
import io.neocore.api.player.group.PermissionEntry;

@DatabaseTable(tableName = "permissions")
public class JdbcPermissionEntry extends AbstractPersistentRecord implements PermissionEntry {
	
	@DatabaseField(foreign = true)
	private JdbcGroup group;
	
	@DatabaseField
	private String context;
	
	@DatabaseField
	private String node;
	
	@DatabaseField
	private boolean state;
	
	public JdbcPermissionEntry() {
		// ORMLite.
	}
	
	public JdbcPermissionEntry(Context context, String node, boolean state) {
		
		this.context = context.getName();
		this.node = node;
		this.state = state;
		
	}
	
	public JdbcPermissionEntry(String node, boolean state) {
		this(null, node, state);
	}
	
	@Override
	public Context getContext() {
		return new LesserContext(this.context);
	}

	@Override
	public String getPermissionNode() {
		return this.node;
	}

	@Override
	public boolean getState() {
		return this.state;
	}
	
}
