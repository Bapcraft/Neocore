package io.neocore.manage.client.network;

import java.util.Set;

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
	
	public void removeEndpoint(String name) {
		this.endpoints.removeIf(e -> e.getEndpointName().equals(name));
	}
	
	@Override
	public Set<NetworkEndpoint> getEndpoints() {
		return this.endpoints;
	}
	
}
