package io.neocore.bukkit.services.network;

import java.net.InetSocketAddress;
import java.util.Set;

import io.neocore.api.NeocoreConfig;
import io.neocore.api.host.proxy.NetworkEndpoint;
import io.neocore.api.host.proxy.NetworkPlayer;

public class SelfEndpoint extends NetworkEndpoint {
	
	private NeocoreConfig config;
	
	public SelfEndpoint(NeocoreConfig conf) {
		this.config = conf;
	}
	
	@Override
	public String getName() {
		return this.config.getServerName();
	}
	
	@Override
	public InetSocketAddress getAddress() {
		return InetSocketAddress.createUnresolved("localhost", 0); // FIXME Make this reflect the actual settings.
	}
	
	@Override
	public Set<NetworkPlayer> getPlayers() {
		
		// TODO Auto-generated method stub
		return null;
		
	}
	
	@Override
	public void sendPlayer(NetworkPlayer player) {
		throw new IllegalStateException("Tried to send a player already on this server to this server.");
	}

}
