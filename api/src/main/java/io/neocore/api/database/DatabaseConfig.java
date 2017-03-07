package io.neocore.api.database;

import com.typesafe.config.Config;

import io.neocore.api.ServiceType;

public interface DatabaseConfig {

	/**
	 * Checks to see if
	 * 
	 * @param type
	 * @return
	 */
	public boolean hasDefinition(ServiceType type);

	/**
	 * Gets the name of the controller that provides the given service.
	 * 
	 * @param type
	 *            The service type who's providing controller we're looking for.
	 * @return The name of the controller.
	 */
	public String getControllerName(ServiceType type);

	/**
	 * 
	 * @param type
	 *            The service type in question.
	 * @return The configuration of the controller we're looking for.
	 */
	public Config getControllerConfig(ServiceType type);

	/**
	 * @return The number of differnt configurations present in the database
	 *         config.
	 */
	public int getNumDiscreteConfigs();

	/**
	 * Gets the config object used in the given controller definition.
	 * 
	 * @param controller
	 *            The name of the controller.
	 * @return The configuration.
	 */
	public Config getConfigForController(String controller);

}
