package io.neocore.jdbc.group;

import java.util.UUID;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import io.neocore.api.database.AbstractPersistentRecord;
import io.neocore.api.host.Context;
import io.neocore.api.host.LesserContext;
import io.neocore.api.player.group.PermissionEntry;

@DatabaseTable(tableName = "permissions")
public class JdbcPermissionEntry extends AbstractPersistentRecord implements PermissionEntry {
	
	@DatabaseField(id = true)
	private UUID id;
	
	@DatabaseField(foreign = true)
	private JdbcGroup group;
	
	@DatabaseField
	private String context;
	
	@DatabaseField
	private String node;
	
	@DatabaseField
	private PermState state;
	
	public JdbcPermissionEntry() {
		this.id = UUID.randomUUID();
	}
	
	public JdbcPermissionEntry(Context context, String node, PermState state) {
		
		this.id = UUID.randomUUID();
		
		this.context = context != null ? context.getName() : null;
		this.node = node;
		this.state = state;
		
	}
	
	public JdbcPermissionEntry(String node, PermState state) {
		this(null, node, state);
	}
	
	@Override
	public Context getContext() {
		return this.context != null ? new LesserContext(this.context) : null;
	}

	@Override
	public String getPermissionNode() {
		return this.node;
	}
	
	public void setState(PermState state) {
		
		this.state = state;
		this.group.dirty();
		this.group.flush();
		
	}
	
	@Override
	public boolean isSet() {
		return this.state != PermState.UNSET;
	}
	
	@Override
	public boolean getState() {
		return this.state == PermState.TRUE;
	}
	
}
