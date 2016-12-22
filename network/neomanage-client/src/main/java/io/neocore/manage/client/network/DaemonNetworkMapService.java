package io.neocore.manage.client.network;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import io.neocore.api.infrastructure.ConnectionFrontend;
import io.neocore.api.infrastructure.NetworkEndpoint;
import io.neocore.api.infrastructure.NetworkMap;
import io.neocore.api.infrastructure.NetworkMapService;

public class DaemonNetworkMapService implements NetworkMapService {
	
	private String networkName;
	private Set<DaemonizedNetworkMap> maps;
	
	public DaemonNetworkMapService(String netName) {
		
		this.networkName = netName;
		this.maps = new HashSet<>();
		
	}
	
	public DaemonizedNetworkMap getNetworkByName(String name) {
		
		for (DaemonizedNetworkMap dnm : this.maps) {
			if (dnm.getNetworkName().equals(name)) return dnm;
		}
		
		return null;
		
	}
	
	public DaemonizedNetworkMap getNetworkByMemberId(UUID id) {
		
		for (DaemonizedNetworkMap dnm : this.maps) {
			if (dnm.containsMember(id)) return dnm;
		}
		
		return null;
		
	}
	
	@Override
	public NetworkMap getLocalNetworkMap() {
		return this.getNetworkByName(this.networkName);
	}
	
	@Override
	public Set<NetworkMap> getGlobalNetworkMaps() {
		
		Set<NetworkMap> maps = new HashSet<>();
		maps.addAll(this.maps);
		return maps;
		
	}
	
	public void unregisterClient(UUID agentId) {
		
		for (DaemonizedNetworkMap dnm : this.maps) {
			dnm.removeMember(agentId);
		}
		
		this.cleanupEmptyNetworks();
		
	}
	
	public void registerFrontend(ConnectionFrontend front) {
		
		if (this.getNetworkByMemberId(front.getAgentId()) != null) throw new IllegalArgumentException("Already registered frontend " + front.getAgentName() + ".");
		DaemonizedNetworkMap map = this.getNetworkByName(front.getNetworkName());
		
		if (map == null) {
			this.maps.add(new DaemonizedNetworkMap(front));
		} else if (map.getFrontend() == null) {
			map.setFrontend(front);
		} else {
			throw new IllegalArgumentException("Network " + front.getNetworkName() + " already has a frontend registered, " + map.getFrontend().getAgentName() + ".");
		}
		
	}
	
	public void registerEndpoint(NetworkEndpoint endpoint) {
		
		if (this.getNetworkByMemberId(endpoint.getAgentId()) != null) throw new IllegalArgumentException("Already registered endpoint " + endpoint.getAgentName() + ".");
		DaemonizedNetworkMap map = this.getNetworkByName(endpoint.getNetworkName());
		
		// We are sure we haven't already added it because of that conditional above.
		map.addEndpoint(endpoint);
		
	}
	
	public void registerStandalone(DaemonizedStandalone agent) {
		
		// Hopefully.
		this.registerFrontend(agent);
		this.registerEndpoint(agent);
		
	}
	
	public void cleanupEmptyNetworks() {
		this.maps.removeIf(n -> n.isEmpty());
	}
	
}
