package io.neocore.api.database;

public interface DatabaseController {
	
	/**
	 * @return The name of the database this controller accesses.
	 */
	public String getBrand();
	
	/**
	 * Called when the database connection should be initialized.
	 */
	public void onEnable();
	
	/**
	 * Called when the database connection should be closed and the library should be shut down.
	 */
	public void onDisable();
	
	/**
	 * Gets a <code>DatabaseServiceProvider</code> for the specified service type.
	 * 
	 * @param type The type of database service to find the provider for.
	 * @return The database's service provider, or null if unsupported.
	 */
	public DatabaseServiceProvider getProvider(DatabaseService type);
	
}
