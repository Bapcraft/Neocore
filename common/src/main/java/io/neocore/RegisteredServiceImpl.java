package io.neocore;

import io.neocore.api.RegisteredService;
import io.neocore.api.ServiceProvider;
import io.neocore.api.ServiceType;
import io.neocore.api.module.Module;

public class RegisteredServiceImpl implements RegisteredService {
	
	private Module providingModule;
	private ServiceType type;
	private ServiceProvider provider;
	
	public RegisteredServiceImpl(Module mod, ServiceType type, ServiceProvider prov) {
		
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
