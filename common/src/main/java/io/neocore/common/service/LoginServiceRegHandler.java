package io.neocore.common.service;

import io.neocore.api.RegisteredService;
import io.neocore.api.host.login.LoginAcceptor;
import io.neocore.api.host.login.LoginService;

public class LoginServiceRegHandler implements ServiceRegistrationHandler {
	
	private LoginService prevService;
	private LoginAcceptor acceptor;
	
	public LoginServiceRegHandler(LoginAcceptor acceptor) {
		this.acceptor = acceptor;
	}
	
	@Override
	public void onRegister(RegisteredService entry) {
		
		if (this.prevService != null) this.prevService.setLoginAcceptor(null);
		
		LoginService serv = (LoginService) entry.getServiceProvider();
		serv.setLoginAcceptor(this.acceptor);
		this.prevService = serv;
		
	}
	
}
