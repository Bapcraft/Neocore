package io.neocore;

import io.neocore.module.Module;

public interface RegisteredService {
	
	/**
	 * @return The module providing this service.
	 */
	public Module getModule();
	
	/**
	 * @return The type of the module 
	 */
	public ServiceType getType();
	
	/**
	 * @return Get the object that actually interacts with the underlying behavior.
	 */
	public ServiceProvider getServiceProvider();
	
	/**
	 * Verifies that the type is actually consistent with the service provider.
	 * 
	 * @return
	 */
	public default boolean verify() {
		return this.getType().getClassType().isAssignableFrom(this.getServiceProvider().getClass());
	}
	
}
