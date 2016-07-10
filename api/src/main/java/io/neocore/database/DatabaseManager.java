package io.neocore.database;

import com.typesafe.config.Config;

public interface DatabaseManager {
	
	public void registerController(String name, Class<? extends DatabaseController> engine);
	
	public DatabaseController makeNewController(String name, Config cfg);
	
}
