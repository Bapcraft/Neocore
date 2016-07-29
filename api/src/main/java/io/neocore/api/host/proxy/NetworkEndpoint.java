package io.neocore.api.host.proxy;

import java.net.InetSocketAddress;
import java.util.Set;

import io.neocore.api.player.NeoPlayer;

public abstract class NetworkEndpoint {
	
	public abstract String getName();
	public abstract InetSocketAddress getAddress();
	
	public abstract Set<NeoPlayer> getPlayers();
	public abstract void sendPlayer(NeoPlayer player);
	
	public int getPlayerCount() {
		return this.getPlayers().size();
	}
	
}
