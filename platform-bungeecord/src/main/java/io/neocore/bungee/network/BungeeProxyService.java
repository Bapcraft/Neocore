package io.neocore.bungee.network;

import io.neocore.api.infrastructure.ProxyAcceptor;
import io.neocore.api.infrastructure.ProxyService;
import io.neocore.bungee.events.PlayerConnectionForwarder;

public class BungeeProxyService implements ProxyService {

	private PlayerConnectionForwarder forwarder;

	public BungeeProxyService(PlayerConnectionForwarder fwdr) {
		this.forwarder = fwdr;
	}

	@Override
	public void setAcceptor(ProxyAcceptor acc) {
		this.forwarder.proxyAcceptor = acc;
	}

	@Override
	public ProxyAcceptor getAcceptor() {
		return this.forwarder.proxyAcceptor;
	}

}
