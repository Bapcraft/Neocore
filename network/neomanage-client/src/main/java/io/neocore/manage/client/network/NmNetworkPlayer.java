package io.neocore.manage.client.network;

import java.net.InetAddress;
import java.util.UUID;

import io.neocore.api.infrastructure.NetworkEndpoint;
import io.neocore.api.infrastructure.NetworkPlayer;

public class NmNetworkPlayer implements NetworkPlayer {
	
	private final UUID uuid;
	
	private InetAddress address;
	
	public NmNetworkPlayer(UUID uuid) {
		this.uuid = uuid;
	}
	
	@Override
	public InetAddress getAddress() {
		return this.address;
	}

	@Override
	public void kick(String message) {
		
	}

	@Override
	public UUID getUniqueId() {
		return this.uuid;
	}

	@Override
	public NetworkEndpoint getDownstreamServer() {
		// TODO Auto-generated method stub
		return null;
	}

}
