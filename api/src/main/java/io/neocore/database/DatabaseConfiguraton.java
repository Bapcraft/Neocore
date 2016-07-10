package io.neocore.database;

import java.util.Collection;

import io.neocore.ServiceType;

public interface DatabaseConfiguraton {
	
	public boolean hasDefinition(ServiceType type);
	public DatabaseController getControllerForService(ServiceType type);
	
	public Collection<DatabaseController> getControllers();
	public DatabaseController getControllerByBrand(String brand);
	
}
