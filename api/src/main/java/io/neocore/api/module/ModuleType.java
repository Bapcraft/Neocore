package io.neocore.api.module;

/**
 * Enumerates the different general types of modules that can be loaded.
 * 
 * @author treyzania
 */
public enum ModuleType {

	/**
	 * Host plugins that provide the Neocore instance and manage its
	 * configuration.
	 */
	HOST,

	/**
	 * Database engines that provide interaction between Neocore and some
	 * database.
	 */
	DATABASE,

	/**
	 * Regular plugins/etc that happen to have Neocore integration should use
	 * this.
	 */
	INTEGRATOR,

	/**
	 * Any modules that run inside of Neocore itself, ignoring the actual
	 * platform.
	 */
	MICROMODULE;

}
