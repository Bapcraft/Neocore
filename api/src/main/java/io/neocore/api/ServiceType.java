package io.neocore.api;

/**
 * Interface for defining a class to be a type of service.  Works well on enums.
 * 
 * @author treyzania
 */
public interface ServiceType {
	
	/**
	 * @return The ALL CAPS name of the service.
	 */
	public String getName();
	
	/**
	 * @return The <code>Class</code> for the interface describing this service.
	 */
	public Class<? extends ServiceProvider> getServiceClass();
	
}
