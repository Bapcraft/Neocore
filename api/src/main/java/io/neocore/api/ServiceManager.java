package io.neocore.api;

import java.util.List;

import io.neocore.api.module.Module;

public interface ServiceManager {
	
	/**
	 * @return A list of ServiceTypes that do not currently have providers.
	 */
	public List<ServiceType> getUnprovidedServices();
	
	/**
	 * Registers a service provider.
	 * 
	 * @param mod The module doing the providing
	 * @param type The type of service being provided
	 * @param service The service provider itself
	 */
	public void registerServiceProvider(Module mod, ServiceType type, ServiceProvider service);
	
	/**
	 * @return A list of service registrations.
	 */
	public List<RegisteredService> getServices();
	
	/**
	 * Gets the service registration for the specified ServiceType
	 * 
	 * @param type The type of service to look for.
	 * @return The service registration.
	 */
	public RegisteredService getService(ServiceType type);
	
	/**
	 * Gets the service provider itself, as the type specified.
	 * 
	 * @param servClazz The interface to find the provider for.
	 * @return The service provider itself.
	 */
	public <T extends ServiceProvider> T getService(Class<T> servClazz);
	
}
