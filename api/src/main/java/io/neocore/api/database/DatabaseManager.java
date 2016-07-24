package io.neocore.api.database;

import java.util.Collection;

import com.typesafe.config.Config;

public interface DatabaseManager {
	
	public void registerController(String name, Class<? extends DatabaseController> engine);
	
	public DatabaseController makeNewController(String name, Config cfg);
	
	public Collection<Class<? extends DatabaseController>> getControllers();
	
}
