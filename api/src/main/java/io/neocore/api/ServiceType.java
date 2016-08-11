package io.neocore.api;

public interface ServiceType {
	
	public String getName();
	public Class<? extends ServiceProvider> getServiceClass();
	
}
