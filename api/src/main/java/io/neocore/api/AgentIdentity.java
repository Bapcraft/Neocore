package io.neocore.api;

import java.util.UUID;

import io.neocore.api.infrastructure.NetworkMember;

public interface AgentIdentity extends NetworkMember {
	
	/**
	 * @return The UUID of the Neocore agent running on this host
	 */
	public UUID getAgentId();
	
	/**
	 * @return A simple, constant, human-readable name for the agent
	 */
	public String getAgentName();
	
}
