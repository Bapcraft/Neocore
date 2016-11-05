package io.neocore.jdbc.player;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import io.neocore.api.eco.Account;

@DatabaseTable(tableName = "accounts")
public class JdbcPlayerAccount implements Account {
	
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
	
	public JdbcPlayerAccount(String curName) {
		this.currency = curName;
	}
	
	@Override
	public void setBalance(float val) {
		
		this.balance = val;
		this.owner.dirty();
		
	}
	
	@Override
	public float getBalance() {
		return this.balance;
	}
	
	@Override
	public void setMultiplier(float mul) {
		
		this.multiplier = mul;
		this.owner.dirty();
		
	}
	
	@Override
	public float getMultiplier() {
		return this.multiplier;
	}
	
}