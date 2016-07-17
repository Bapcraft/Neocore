package io.neocore.api.eco;

public interface Account {
	
	public void setBalance(float val);
	public float getBalance();
	
	public default float deposit(float val) {
		
		float newQty = this.getBalance() + val;
		this.setBalance(newQty);
		
		return newQty;
		
	}
	
	public default float withdraw(float val) {
		
		float newQty = this.getBalance() - val;
		this.setBalance(newQty);
		
		return newQty;
		
	}
	
}
