package io.neocore.api.infrastructure;

import io.neocore.api.ServiceProvider;
import io.neocore.api.ServiceType;

public enum InfrastructureService implements ServiceType {
	
	PROXY(ProxyService.class),
	ENDPOINT(EndpointService.class),
	NETWORKMAP(NetworkMapService.class); // Stores data for the state of the local network.
	
	private Class<? extends InfraServiceProvider> serviceClass;
	
	private InfrastructureService(Class<? extends InfraServiceProvider> clazz) {
		this.serviceClass = clazz;
	}
	
	@Override
	public String getName() {
		return this.name();
	}
	
	@Override
	public Class<? extends ServiceProvider> getServiceClass() {
		return this.serviceClass;
	}
	
}
