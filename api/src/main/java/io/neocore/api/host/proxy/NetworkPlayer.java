package io.neocore.api.host.proxy;

import io.neocore.api.host.ConnectingPlayer;

public interface NetworkPlayer extends ConnectingPlayer {
	
	public NetworkEndpoint getDownstreamServer();
	
}
