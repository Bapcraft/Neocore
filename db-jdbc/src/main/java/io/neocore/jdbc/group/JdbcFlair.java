package io.neocore.jdbc.group;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import io.neocore.api.player.group.Flair;

@DatabaseTable(tableName = "groupFlairs")
public class JdbcFlair implements Flair {

	@DatabaseField(generatedId = true, allowGeneratedIdInsert = true)
	private int flairId;
	
	@DatabaseField(canBeNull = false, foreign = true)
	private JdbcGroup owner;
	
	@DatabaseField(canBeNull = false)
	private String prefix = "";
	
	@DatabaseField(canBeNull = false)
	private String suffix = "";
	
	public JdbcFlair() {
		
	}
	
	public JdbcFlair(String pre, String suf) {
		
		this.prefix = pre;
		this.suffix = suf;
		
	}
	
	@Override
	public void setPrefix(String prefix) {
		
		this.prefix = prefix;
		this.owner.dirty();
		
	}
	
	@Override
	public String getPrefix() {
		return this.prefix;
	}
	
	@Override
	public void setSuffix(String suffix) {
		
		this.suffix = suffix;
		this.owner.dirty();
		
	}
	
	@Override
	public String getSuffix() {
		return this.suffix;
	}
	
}
