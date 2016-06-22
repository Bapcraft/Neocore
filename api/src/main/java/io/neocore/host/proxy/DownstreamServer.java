package io.neocore.host.proxy;

import java.net.InetSocketAddress;

public interface DownstreamServer {
	
	public String getName();
	public InetSocketAddress getAddress();
	
}
