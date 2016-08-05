package io.neocore.api.database;

import com.typesafe.config.Config;

import io.neocore.api.ServiceType;

public interface DatabaseConfig {
	
	public boolean hasDefinition(ServiceType type);
	
	public String getControllerName(ServiceType type);
	public Config getControllerConfig(ServiceType type);
	
	public int getNumDiscreteConfigs();
	
}
