package io.neocore.host.proxy;

import io.neocore.host.ConnectingPlayer;

public interface ProxiedPlayer extends ConnectingPlayer {
	
	public DownstreamServer getDownstreamServer();
	
}
