package io.neocore.bungee.network;

import java.util.Set;
import java.util.UUID;

import io.neocore.api.infrastructure.NetworkEndpoint;
import io.neocore.api.infrastructure.NetworkPlayer;
import net.md_5.bungee.api.config.ServerInfo;

public class BungeeDownstreamEndpoint implements NetworkEndpoint {
	
	private UUID id;
	private String bungeeName;
	private ServerInfo server;
	
	public BungeeDownstreamEndpoint(UUID id, String bungeeName, ServerInfo serv) {
		
		this.bungeeName = bungeeName;
		this.server = serv;
		
	}
	
	@Override
	public UUID getAgentId() {
		return this.id;
	}
	
	@Override
	public String getNetworkName() {
		return this.bungeeName;
	}
	
	@Override
	public String getAgentName() {
		return this.server.getName();
	}
	
	@Override
	public Set<NetworkPlayer> getPlayers() {
		return null; // TODO Make this return proper values.
	}
	
}
