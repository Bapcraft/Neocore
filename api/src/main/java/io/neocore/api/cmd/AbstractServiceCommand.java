package io.neocore.api.cmd;

import io.neocore.api.ServiceManager;
import io.neocore.api.ServiceProvider;

public abstract class AbstractServiceCommand extends AbstractCommand {
	
	private ServiceManager services;
	
	public AbstractServiceCommand(String name, ServiceManager services) {
		
		super(name);
		
		this.services = services;
		
	}
	
	protected ServiceManager getServiceManager() {
		return this.services;
	}
	
	protected <T extends ServiceProvider> T getService(Class<T> clazz) {
		return this.getServiceManager().getService(clazz);
	}
	
}
