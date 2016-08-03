package io.neocore.api.host.proxy;

public interface ProxyProvider extends NetworkParticipant {
	
	public void setProxyAcceptor(ProxyAcceptor acceptor);
	public ProxyAcceptor getProxyAcceptor();
	
}
