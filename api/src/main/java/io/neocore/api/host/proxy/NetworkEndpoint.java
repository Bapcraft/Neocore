package io.neocore.api.host.proxy;

import java.net.InetSocketAddress;
import java.util.Set;

public abstract class NetworkEndpoint {
	
	public abstract String getName();
	public abstract InetSocketAddress getAddress();
	
	public abstract Set<NetworkPlayer> getPlayers();
	public abstract void sendPlayer(NetworkPlayer player);
	
	public int getPlayerCount() {
		return this.getPlayers().size();
	}
	
}
