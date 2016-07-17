package io.neocore.api;

import java.util.List;

import io.neocore.api.module.Module;

public interface ServiceManager {
	
	public List<ServiceType> getUnprovidedServices();
	
	public void registerServiceProvider(Module mod, ServiceType type, ServiceProvider provider);
	
	public List<RegisteredService> getServices();
	public RegisteredService getService(ServiceType type);
	public <T extends ServiceProvider> T getService(Class<T> servClazz);
	
}
