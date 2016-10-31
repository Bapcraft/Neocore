package io.neocore.mysql.player;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import io.neocore.api.NeocoreAPI;
import io.neocore.api.database.AbstractPersistentRecord;
import io.neocore.api.player.extension.Extension;

@DatabaseTable(tableName = "playerExtensions")
public class MysqlExtensionRecord extends AbstractPersistentRecord {
	
	@DatabaseField(canBeNull = false, foreign = true)
	protected MysqlDbPlayer owner;
	
	@DatabaseField(canBeNull = false)
	protected String type;
	
	@DatabaseField(canBeNull = false)
	protected String data;
	
	public MysqlExtensionRecord() {
		// ORMLite.
	}
	
	public MysqlExtensionRecord(Extension ext) {
		
		this.type = ext.getName();
		this.data = ext.serialize();
		
	}
	
	public Extension deserialize() {
		
		// FIXME Muh encapsulation!
		return NeocoreAPI.getAgent().getExtensionManager().deserialize(this.type, this.data);
		
	}
	
}
