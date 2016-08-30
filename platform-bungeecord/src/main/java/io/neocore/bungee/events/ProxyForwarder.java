package io.neocore.bungee.events;

import io.neocore.api.host.proxy.ProxyAcceptor;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.event.EventHandler;

public class ProxyForwarder extends EventForwarder {

	public ProxyAcceptor dest;
	
	@EventHandler
	public void onDownstreamTransfer(ServerSwitchEvent event) {
		// TODO
	}
	
}
