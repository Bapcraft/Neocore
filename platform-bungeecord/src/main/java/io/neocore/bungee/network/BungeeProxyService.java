package io.neocore.bungee.network;

import io.neocore.api.infrastructure.ProxyAcceptor;
import io.neocore.api.infrastructure.ProxyService;

public class BungeeProxyService implements ProxyService {
	
	private ProxyAcceptor acceptor;
	
	public BungeeProxyService() {
		// Not needed?
	}

	@Override
	public void setAcceptor(ProxyAcceptor acc) {
		this.acceptor = acc;
	}

	@Override
	public ProxyAcceptor getAcceptor() {
		return this.acceptor;
	}
	
}
