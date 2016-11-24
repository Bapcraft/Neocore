package io.neocore.common.service;

import io.neocore.api.RegisteredService;

@FunctionalInterface
public interface ServiceRegistrationHandler {
	
	public void onRegister(RegisteredService entry);
	
}
