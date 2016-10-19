package io.neocore.manage.client;

import java.net.InetSocketAddress;

public class NmServer {
	
	private InetSocketAddress address;
	
	public NmServer(InetSocketAddress addr) {
		this.address = addr;
	}
	
	public InetSocketAddress getRemoteSocket() {
		return this.address;
	}
	
	
	
}
