package io.neocore.api.database.transactions;

import io.neocore.api.database.DatabaseServiceProvider;
import io.neocore.api.eco.Transaction;

public interface TransactionLogProvider extends DatabaseServiceProvider {
	
	public void log(Transaction transaction);
	
}
