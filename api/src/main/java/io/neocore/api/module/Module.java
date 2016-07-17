package io.neocore.api.module;

import io.neocore.api.NeocoreAPI;
import io.neocore.api.ServiceProvider;
import io.neocore.api.ServiceType;

public interface Module {
	
	public String getName();
	public String getVersion();
	
	public ModuleType getModuleType();
	
	public default void registerService(ServiceType type, ServiceProvider provider) {
		NeocoreAPI.getAgent().getServiceManager().registerServiceProvider(this, type, provider);
	}
	
}
