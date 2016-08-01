package io.neocore.api.host.login;

public interface LoginAcceptor {
	
	public void onInitialLoginEvent(InitialLoginEvent event);
	public void onPostLoginEvent(PostLoginEvent event);
	public void onDisconnectEvent(DisconnectEvent event);
	
}
