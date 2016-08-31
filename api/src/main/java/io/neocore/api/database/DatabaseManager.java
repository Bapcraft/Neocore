package io.neocore.api.database;

import java.util.Collection;

/**
 * Manages registration, initialization, and delegation of database controllers.<br/>
 * Shouldn't be used to query services. @see ServiceManager
 * 
 * @author treyzania
 */
public interface DatabaseManager {
	
	/**
	 * Registers the controller as a given name,
	 * 
	 * @param name The name to give the controller.
	 * @param engine The class of the controller.
	 */
	public void registerController(String name, Class<? extends DatabaseController> engine);
	
	/**
	 * Gets the database controller with the specified name.  Will not instantiate controllers.
	 * 
	 * @param name The controller's name.
	 * @return The controller.
	 */
	public DatabaseController getControllerForName(String name);
	
	/**
	 * @return A collection of all of the classes for the database controllers.
	 */
	public Collection<Class<? extends DatabaseController>> getControllerClasses();
	
	/**
	 * @return A collection of all of the instantiated database controllers.
	 */
	public Collection<DatabaseController> getControllers();
	
}
