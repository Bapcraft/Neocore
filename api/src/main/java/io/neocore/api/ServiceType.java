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
	
	/**
	 * Checks to see if the given service provider is compatible with this service type.
	 * 
	 * @param prov The provider.
	 * @return <code>true</code> if it is compatible, <code>false</code> otherwise.
	 */
	public default boolean isCompatible(ServiceProvider prov) {
		return this.getServiceClass() != null && prov != null && this.getServiceClass().isAssignableFrom(prov.getClass());
	}
	
}
