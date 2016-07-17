package io.neocore.api.host.proxy;

import io.neocore.api.host.ConnectingPlayer;

public interface ProxiedPlayer extends ConnectingPlayer {
	
	public DownstreamServer getDownstreamServer();
	
}
