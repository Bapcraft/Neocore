package io.neocore.api.module;

import io.neocore.api.NeocoreAPI;
import io.neocore.api.ServiceProvider;
import io.neocore.api.ServiceType;

/**
 * Interface representing something that can interact strongly with Neocore.
 * 
 * @author treyzania
 */
public interface Module {

	/**
	 * @return The name of the Module.
	 */
	public String getName();

	/**
	 * @return The "version" of the Module.
	 */
	public String getVersion();

	/**
	 * @return The type of module.
	 */
	public ModuleType getModuleType();

	/**
	 * Registers a service in the main service manager using this module.
	 * 
	 * @param type
	 *            The type of service.
	 * @param service
	 *            The service itself.
	 */
	public default void registerService(ServiceType type, ServiceProvider service) {

		// FIXME Breaks encapsulation?
		NeocoreAPI.getAgent().getServiceManager().registerServiceProvider(this, type, service);

	}

}
