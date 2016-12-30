package io.neocore.jdbc.player;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import io.neocore.api.database.AbstractPersistentRecord;
import io.neocore.api.eco.Account;

@DatabaseTable(tableName = "accounts")
public class JdbcPlayerAccount extends AbstractPersistentRecord implements Account {

	@DatabaseField(generatedId = true, allowGeneratedIdInsert = true)
	private int accountId;
	
	@DatabaseField(canBeNull = false, foreign = true)
	private JdbcDbPlayer owner;
	
	@DatabaseField(canBeNull = false)
	public String currency;
	
	@DatabaseField
	private float balance;
	
	@DatabaseField
	private float multiplier = 1F;
	
	public JdbcPlayerAccount() {
		// ORMLite.
	}
	
	public JdbcPlayerAccount(JdbcDbPlayer owner, String curName) {
		
		this.owner = owner;
		this.currency = curName;
		
	}
	
	@Override
	public void setBalance(float val) {
		
		this.balance = val;
		this.dirty();
		
	}
	
	@Override
	public float getBalance() {
		return this.balance;
	}
	
	@Override
	public void setMultiplier(float mul) {
		
		this.multiplier = mul;
		this.dirty();
		
	}
	
	@Override
	public float getMultiplier() {
		return this.multiplier;
	}

	@Override
	public void dirty() {
		
		this.owner.dirty();
		this.owner.updatedAccounts.add(this);
		
	}

	@Override
	public boolean isDirty() {
		return this.owner.isDirty();
	}
	
}
