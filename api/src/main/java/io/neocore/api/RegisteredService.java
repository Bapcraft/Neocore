package io.neocore.api;

import io.neocore.api.module.Module;

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
	 * @return If this is a valid registered service.
	 */
	public default boolean verify() {
		return this.getType().getServiceClass().isAssignableFrom(this.getServiceProvider().getClass());
	}
	
}
