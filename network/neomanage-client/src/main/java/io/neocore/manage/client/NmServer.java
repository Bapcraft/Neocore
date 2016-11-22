package io.neocore.manage.client;

import java.net.InetSocketAddress;

public class NmServer {
	
	private boolean isOnline;
	private InetSocketAddress address;
	
	public NmServer(InetSocketAddress addr) {
		this.address = addr;
	}
	
	public boolean isOnline() {
		return this.isOnline;
	}
	
	public InetSocketAddress getRemoteSocket() {
		return this.address;
	}
	
}
