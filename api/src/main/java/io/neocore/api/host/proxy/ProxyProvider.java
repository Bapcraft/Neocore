package io.neocore.api.host.proxy;

public interface ProxyProvider extends NetMemberProvider {
	
	/**
	 * Sets the acceptor for proxy events.
	 * 
	 * @param acceptor The acceptor.
	 */
	public void setProxyAcceptor(ProxyAcceptor acceptor);
	
	/**
	 * @return The proxy event acceptor.
	 */
	public ProxyAcceptor getProxyAcceptor();
	
}
