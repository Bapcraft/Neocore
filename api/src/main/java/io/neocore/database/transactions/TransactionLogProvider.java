package io.neocore.database.transactions;

import io.neocore.database.DatabaseServiceProvider;
import io.neocore.eco.Transaction;

public interface TransactionLogProvider extends DatabaseServiceProvider {
	
	public void log(Transaction transaction);
	
}
