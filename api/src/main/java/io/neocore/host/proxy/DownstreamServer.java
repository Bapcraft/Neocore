package io.neocore.host.proxy;

import java.net.InetAddress;

public interface DownstreamServer {
	
	public String getName();
	public InetAddress getAddress();
	
}
