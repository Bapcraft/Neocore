package io.neocore.host.login;

import io.neocore.host.HostServiceProvider;

public interface LoginProvider extends HostServiceProvider {
	
	public void setLoginAcceptor(LoginAcceptor acceptor);
	public LoginAcceptor getLoginAcceptor();
	
}
