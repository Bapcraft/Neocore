package io.neocore.api.eco;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

/**
 * Represents a transaction of money between two accounts. There is (currently)
 * no validation that the accounts are of the same type of currency.
 * 
 * @author treyzania
 */
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

	/**
	 * Returns the UUID of the transaction. This has no correlation to the UUIDs
	 * of the two account owners, if any.
	 * 
	 * @return The UUID of the transaction.
	 */
	public UUID getTransactionId() {
		return this.id;
	}

	/**
	 * @return The account sending the money.
	 */
	public Account getSender() {
		return this.sender;
	}

	/**
	 * @return The account receiving the money.
	 */
	public Account getReceiver() {
		return this.receiver;
	}

	/**
	 * @return The amount of currency trnasferred.
	 */
	public float getTransferredValue() {
		return this.value;
	}

	/**
	 * @return The amount of currency the sender had before the transaction.
	 */
	public float getSenderInitialBalance() {
		return this.senderPreBalance;
	}

	/**
	 * @return The amount of currency the receiver had before the transaction.
	 */
	public float getReceiverInitialBalance() {
		return this.recPreBalance;
	}

	/**
	 * @return <code>true</code> if the transaction has been performed,
	 *         <code>false</code> otherwise.
	 */
	public boolean isCompleted() {
		return !Float.isNaN(this.senderPreBalance) && !Float.isNaN(this.recPreBalance) && (this.time != null);
	}

	/**
	 * Performs the transaction, moving the currency around.
	 */
	public void transact() {

		synchronized (this) {

			if (this.isCompleted())
				throw new IllegalStateException("Transaction has already occurred!");

			Account s = this.getSender();
			Account r = this.getReceiver();

			this.senderPreBalance = s.getBalance();
			this.recPreBalance = r.getBalance();

			this.time = Date.from(Instant.now());

			s.withdraw(this.value);
			r.deposit(this.value);

		}

	}

	/**
	 * @return Checks to see if the sender has sufficient funds.
	 */
	public boolean hasSenderSufficientFunds() {
		return this.getSender().getBalance() >= this.getTransferredValue();
	}

}
