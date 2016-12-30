package io.neocore.jdbc.player;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import io.neocore.api.NeocoreAPI;
import io.neocore.api.player.extension.Extension;

@DatabaseTable(tableName = "playerExtensions")
public class JdbcExtensionRecord {
	
	@DatabaseField(canBeNull = false, foreign = true)
	protected JdbcDbPlayer owner;
	
	@DatabaseField(canBeNull = false)
	protected String type;
	
	@DatabaseField(canBeNull = false)
	protected String data;
	
	public JdbcExtensionRecord() {
		// ORMLite.
	}
	
	public JdbcExtensionRecord(Extension ext) {
		
		this.type = ext.getName();
		this.data = NeocoreAPI.getAgent().getExtensionManager().serialize(ext); // FIXME Breaks encapsulation.
		
	}
	
	public Extension deserialize() {
		
		// FIXME Breaks encapsulation.
		return NeocoreAPI.getAgent().getExtensionManager().deserialize(this.type, this.data);
		
	}
	
}
