package io.neocore.manage.client.network;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import io.neocore.api.NeocoreAPI;
import io.neocore.api.infrastructure.ConnectionFrontend;
import io.neocore.api.infrastructure.NetworkEndpoint;
import io.neocore.api.infrastructure.NetworkMap;

public class DaemonizedNetworkMap implements DaemonizedNetworkComponent, NetworkMap {
	
	private ConnectionFrontend frontend;
	private Set<NetworkEndpoint> endpoints;
	
	public DaemonizedNetworkMap(ConnectionFrontend frontend, Set<NetworkEndpoint> endpoints) {
		
		this.frontend = frontend;
		this.endpoints = endpoints;
		
	}
	
	public DaemonizedNetworkMap(ConnectionFrontend frontend) {
		this(frontend, new HashSet<>());
	}
	
	public DaemonizedNetworkMap(NetworkEndpoint singularEndpoint) {
		this(null, new HashSet<>(Arrays.asList(singularEndpoint)));
	}
	
	public void setFrontend(ConnectionFrontend front) {
		this.frontend = front;
	}
	
	@Override
	public ConnectionFrontend getFrontend() {
		return this.frontend;
	}
	
	public void addEndpoint(NetworkEndpoint ep) {
		this.endpoints.add(ep);
	}
	
	public void removeEndpoint(UUID id) {
		this.endpoints.removeIf(e -> e.getAgentId().equals(id));
	}
	
	@Override
	public Set<NetworkEndpoint> getEndpoints() {
		return this.endpoints;
	}
	
	public boolean isEmpty() {
		return this.frontend == null && this.endpoints.isEmpty();
	}
	
	public boolean isValid() {
		return this.frontend != null && !this.endpoints.isEmpty();
	}
	
	public boolean containsMember(UUID uuid) {
		
		if (this.frontend.getAgentId().equals(uuid)) return true;
		
		for (NetworkEndpoint ne : this.endpoints) {
			if (ne.getAgentId().equals(uuid)) return true;
		}
		
		return false;
		
	}
	
	public void removeMember(UUID uuid) {
		
		if (this.frontend.getAgentId().equals(uuid)) this.frontend = null;
		this.endpoints.removeIf(e -> e.getAgentId().equals(uuid));
		
	}
	
	public String getNetworkName() {
		
		if (this.getFrontend() != null) {
			return this.getFrontend().getNetworkName();
		} else if (this.endpoints.size() > 0) {
			return this.endpoints.iterator().next().getNetworkName(); // XXX Not perfect, but okay.
		} else {
			
			NeocoreAPI.getLogger().warning("Tried to get the name of a network that has no members at all.");
			return "[undefined]";
			
		}
		
	}
	
}
