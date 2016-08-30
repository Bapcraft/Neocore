package io.neocore.bukkit.services.network;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;

import com.justisr.BungeeCom;

import io.neocore.api.NeocoreConfig;
import io.neocore.api.host.proxy.EndpointService;
import io.neocore.api.host.proxy.NetworkEndpoint;
import io.neocore.api.host.proxy.NetworkPlayer;

public class BukkitEndpointService implements EndpointService {
	
	private SelfEndpoint self;
	
	public BukkitEndpointService(SelfEndpoint self) {
		this.self = self;
	}
	
	@Override
	public Set<NetworkEndpoint> getNetworkEndpoints() {
		return Collections.emptySet();
	}
	
	@Override
	public void move(NetworkPlayer player, NetworkEndpoint server) {
		// TODO Carry username information in NetworkPlayer to make this work.
	}
	
	@Override
	public NetworkPlayer getPlayer(UUID uuid) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public NetworkEndpoint getServerEndpoint() {
		return this.self;
	}
	
}
