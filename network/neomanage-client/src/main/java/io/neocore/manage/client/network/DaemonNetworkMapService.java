package io.neocore.manage.client.network;

import java.util.HashSet;
import java.util.Set;

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
		
		for (DaemonizedNetworkMap dmm : this.maps) {
			if (dmm.getFrontend().getNetworkName().equals(name)) return dmm;
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
	
}
