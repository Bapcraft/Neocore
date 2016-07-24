package io.neocore.api.host.broadcast;

import io.neocore.api.host.HostServiceProvider;

public interface BroadcastProvider extends HostServiceProvider {
	
	public void broadcast(String message);
	
}