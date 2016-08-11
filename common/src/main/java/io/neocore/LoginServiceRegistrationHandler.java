package io.neocore;

import io.neocore.api.RegisteredService;
import io.neocore.api.host.login.LoginAcceptor;
import io.neocore.api.host.login.LoginProvider;

public class LoginServiceRegistrationHandler implements ServiceRegistrationHandler {
	
	private LoginProvider previousProvider;
	private LoginAcceptor acceptor;
	
	public LoginServiceRegistrationHandler(LoginAcceptor acceptor) {
		this.acceptor = acceptor;
	}
	
	@Override
	public void onRegister(RegisteredService entry) {
		
		if (this.previousProvider != null) this.previousProvider.setLoginAcceptor(null);
		
		LoginProvider prov = (LoginProvider) entry.getServiceProvider();
		prov.setLoginAcceptor(this.acceptor);
		this.previousProvider = prov;
		
	}
	
}
