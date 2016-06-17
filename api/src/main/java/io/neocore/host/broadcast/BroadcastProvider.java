package io.neocore.host.broadcast;

import io.neocore.host.HostServiceProvider;

public interface BroadcastProvider extends HostServiceProvider {
	
	public void broadcast(String message);
	
}
