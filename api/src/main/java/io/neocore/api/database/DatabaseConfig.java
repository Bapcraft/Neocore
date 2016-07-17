package io.neocore.api.database;

import java.util.Collection;

import io.neocore.api.ServiceType;

public interface DatabaseConfig {
	
	public boolean hasDefinition(ServiceType type);
	public DatabaseController getControllerForService(ServiceType type);
	
	public Collection<DatabaseController> getControllers();
	public DatabaseController getControllerByBrand(String brand);
	
}
