package io.neocore.host.login;

public interface LoginAcceptor {
	
	public void onInitialLoginEvent(InitialLoginEvent event);
	public void onPostLoginEvent(PostLoginEvent event);
	
}
