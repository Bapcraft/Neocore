package io.neocore.bukkit.services.network;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import io.neocore.api.infrastructure.NetworkMap;
import io.neocore.api.infrastructure.NetworkMapService;

public class StandaloneNetworkMapService implements NetworkMapService {
	
	private StandaloneNetworkMember self;
	private StandaloneNetworkMap map;
	
	public StandaloneNetworkMapService(String fakeName) {
		
		this.self = new StandaloneNetworkMember(fakeName);
		this.map = new StandaloneNetworkMap(this.self);
		
	}
	
	@Override
	public NetworkMap getLocalNetworkMap() {
		return this.map;
	}
	
	@Override
	public Set<NetworkMap> getGlobalNetworkMaps() {
		return new HashSet<>(Arrays.asList(this.getLocalNetworkMap()));
	}
	
}
