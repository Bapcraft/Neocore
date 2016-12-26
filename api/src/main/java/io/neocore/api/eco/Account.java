package io.neocore.api.eco;

import io.neocore.api.database.Persistent;

/**
 * A bank account interface representing the amount of a specific currency in
 * one location.  This location is usually a player's personal, universal
 * account.
 * 
 * @author treyzania
 */
public interface Account extends Persistent {
	
	/**
	 * Sets the balance of the account's currency to the specified amount.
	 * 
	 * @param val The value to set the account to.
	 */
	public void setBalance(float val);
	
	/**
	 * Gets the balance of the account.
	 * 
	 * @return The balance of the account.
	 */
	public float getBalance();
	
	/**
	 * Adds the specified amount of money to the account.
	 * 
	 * @param val The value to add.
	 * @return The value after the transaction.
	 */
	public default float deposit(float val) {
		
		float newQty = this.getBalance() + val;
		this.setBalance(newQty);
		
		return newQty;
		
	}
	
	/**
	 * Removed the specified amount of money from the account.
	 * 
	 * @param val The value to remove.
	 * @return The value after the transaction.
	 */
	public default float withdraw(float val) {
		
		float newQty = this.getBalance() - val;
		this.setBalance(newQty);
		
		return newQty;
		
	}
	
	/**
	 * Sets the multiplier that this account could optionally use.
	 * 
	 * @param mul The multiplier.
	 */
	public void setMultiplier(float mul);
	
	/**
	 * Gets the multiplier that this account could optionally use.
	 * 
	 * @return The account's multiplier.
	 */
	public float getMultiplier();
	
}
