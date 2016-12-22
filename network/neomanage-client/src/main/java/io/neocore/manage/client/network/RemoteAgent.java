package io.neocore.manage.client.network;

import java.util.UUID;

import io.neocore.api.AgentIdentity;

public class RemoteAgent implements AgentIdentity, DaemonizedNetworkComponent {
	
	private UUID agentId;
	private String name, network;
	
	public RemoteAgent(UUID id, String name, String network) {
		
		this.agentId = id;
		this.name = name;
		this.network = network;
		
	}
	
	public RemoteAgent(UUID id, String name) {
		this(id, name, null);
	}
	
	@Override
	public UUID getAgentId() {
		return this.agentId;
	}
	
	@Override
	public String getAgentName() {
		return this.name;
	}
	
	@Override
	public String getNetworkName() {
		return this.network;
	}
	
}
