package io.neocore.api.database;

import java.util.Collection;

public interface DatabaseManager {
	
	public void registerController(String name, Class<? extends DatabaseController> engine);
	
	public DatabaseController getControllerForName(String name);
	
	public Collection<Class<? extends DatabaseController>> getControllerClasses();
	public Collection<DatabaseController> getControllers();
	
}
