package io.neocore.manage.client.network;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import io.neocore.api.infrastructure.NetworkMap;
import io.neocore.api.infrastructure.NetworkMapService;

public class NmNetworkMapService implements NetworkMapService {
	
	private String networkName;
	private Set<NmNetworkMap> maps;
	
	public NmNetworkMapService(String netName) {
		
		this.networkName = netName;
		this.maps = new HashSet<>();
		
	}
	
	public NmNetworkMap getNetworkByName(String name) {
		
		for (NmNetworkMap dnm : this.maps) {
			if (dnm.getNetworkName().equals(name)) return dnm;
		}
		
		return null;
		
	}
	
	public NmNetworkMap getNetworkByMemberId(UUID id) {
		
		for (NmNetworkMap dnm : this.maps) {
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
		
		for (NmNetworkMap dnm : this.maps) {
			dnm.removeMember(agentId);
		}
		
		this.cleanupEmptyNetworks();
		
	}
	
	public void registerFrontend(NmFrontend front) {
		
		if (this.getNetworkByMemberId(front.getAgentId()) != null) throw new IllegalArgumentException("Already registered frontend " + front.getAgentName() + ".");
		NmNetworkMap map = this.getNetworkByName(front.getNetworkName());
		
		if (map == null && front instanceof NmProxyFrontend) {
			this.maps.add(new NmNetworkMap((NmProxyFrontend) front));
		} else if (map.getFrontend() == null) {
			map.setFrontend(front);
		} else {
			throw new IllegalArgumentException("Network " + front.getNetworkName() + " already has a frontend registered, " + map.getFrontend().getAgentName() + ".");
		}
		
	}
	
	public void registerEndpoint(NmEndpoint endpoint) {
		
		if (this.getNetworkByMemberId(endpoint.getAgentId()) != null) throw new IllegalArgumentException("Already registered endpoint " + endpoint.getAgentName() + ".");
		NmNetworkMap map = this.getNetworkByName(endpoint.getNetworkName());
		
		// We are sure we haven't already added it because of that conditional above.
		map.addEndpoint(endpoint);
		
	}
	
	public void registerStandalone(NmStandalone agent) {
		
		// Hopefully.
		this.registerFrontend(agent);
		this.registerEndpoint(agent);
		
	}
	
	public void cleanupEmptyNetworks() {
		this.maps.removeIf(n -> n.isEmpty());
	}
	
	public NmNetworkComponent getComponentById(UUID id) {
		
		NmNetworkMap dnm = this.getNetworkByMemberId(id);
		if (dnm == null) return null;
		
		if (dnm.getFrontend() != null && dnm.getFrontend().getAgentId().equals(id)) {
			return dnm.getFrontend_Nm();
		} else {
			
			for (NmEndpoint nep : dnm.getEndpoints_Nm()) {
				if (nep.getAgentId().equals(id)) return nep;
			}
			
		}
		
		return null;
		
	}
	
}
