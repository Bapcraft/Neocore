package io.neocore.api.database;

/**
 * Agent for providing database services by following the configuration.
 * 
 * @author treyzania
 */
public interface DatabaseController {
	
	/**
	 * @return The name of the database this controller accesses.
	 */
	public String getBrand();
	
	/**
	 * Called when the database connection should be initialized.
	 */
	public void initialize();
	
	/**
	 * Called when the database connection should be closed and the library should be shut down.
	 */
	public void shutdown();
	
	/**
	 * Returns an array of service providers who map index-to-index with the
	 * service definition supplied in the given array.  Should only be called
	 * once.
	 * 
	 * @param services An array of services to provide for.
	 * @return The array of matching service providers to the first array.
	 */
	public DatabaseServiceProvider[] provide(DatabaseService[] services);
	
}
