package io.neocore.api.host.proxy;

import java.net.InetSocketAddress;

public interface DownstreamServer {
	
	public String getName();
	public InetSocketAddress getAddress();
	
}
