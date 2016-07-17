package io.neocore.api.eco;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

public class Transaction {
	
	private final UUID id;
	private Date time;
	
	private final Account sender, receiver;
	private final float value;
	
	private float senderPreBalance, recPreBalance;
	
	public Transaction(Account sender, Account receiver, float val) {
		
		this.id = UUID.randomUUID();
		
		this.sender = sender;
		this.receiver = receiver;
		
		this.value = val;
		
		this.senderPreBalance = Float.NaN;
		this.recPreBalance = Float.NaN;
		
	}
	
	public UUID getTransactionId() {
		return this.id;
	}
	
	public Account getSender() {
		return this.sender;
	}
	
	public Account getReceiver() {
		return this.receiver;
	}
	
	public float getTransferredValue() {
		return this.value;
	}
	
	public float getSenderInitialBalance() {
		return this.senderPreBalance;
	}
	
	public float getReceiverInitialBalance() {
		return this.recPreBalance;
	}
	
	public boolean isCompleted() {
		return !Float.isNaN(this.senderPreBalance) && !Float.isNaN(this.recPreBalance) && (this.time != null);
	}
	
	public void transact() {
		
		if (this.isCompleted()) throw new IllegalStateException("Transaction has already occurred!");
		
		Account s = this.getSender();
		Account r = this.getReceiver();
		
		this.senderPreBalance = s.getBalance();
		this.recPreBalance = r.getBalance();
		
		this.time = Date.from(Instant.now());
		
		s.withdraw(this.value);
		r.deposit(this.value);
		
	}
	
	public boolean hasSenderSufficientFunds() {
		return this.getSender().getBalance() >= this.getTransferredValue();
	}
	
}
