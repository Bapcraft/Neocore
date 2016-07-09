package io.neocore;

import io.neocore.RegisteredService;
import io.neocore.ServiceProvider;
import io.neocore.ServiceType;
import io.neocore.module.Module;

public class ServiceRegistration implements RegisteredService {
	
	private Module providingModule;
	private ServiceType type;
	private ServiceProvider provider;
	
	public ServiceRegistration(Module mod, ServiceType type, ServiceProvider prov) {
		
		this.providingModule = mod;
		this.type = type;
		this.provider = prov;
		
	}
	
	@Override
	public Module getModule() {
		return this.providingModule;
	}
	
	@Override
	public ServiceType getType() {
		return this.type;
	}
	
	@Override
	public ServiceProvider getServiceProvider() {
		return this.provider;
	}
	
}
