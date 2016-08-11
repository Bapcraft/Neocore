package io.neocore;

import io.neocore.api.RegisteredService;

public interface ServiceRegistrationHandler {
	
	public void onRegister(RegisteredService entry);
	
}
