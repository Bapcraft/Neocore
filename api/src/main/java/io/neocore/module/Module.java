package io.neocore.module;

import io.neocore.NeocoreAPI;
import io.neocore.ServiceProvider;
import io.neocore.ServiceType;

public interface Module {
	
	public String getName();
	public String getVersion();
	
	public ModuleType getModuleType();
	
	public default void registerService(ServiceType type, ServiceProvider provider) {
		NeocoreAPI.getAgent().getServiceManager().registerServiceProvider(this, type, provider);
	}
	
}
