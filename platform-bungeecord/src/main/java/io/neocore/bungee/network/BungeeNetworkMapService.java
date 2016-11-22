package io.neocore.bungee.network;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import io.neocore.api.infrastructure.NetworkMap;
import io.neocore.api.infrastructure.NetworkMapService;
import net.md_5.bungee.api.ProxyServer;

public class BungeeNetworkMapService implements NetworkMapService {
	
	private BungeeFrontend frontend;
	private DownstreamWrapper downstream;
	
	private BungeeNetworkMap map;
	
	public BungeeNetworkMapService(String netName, ProxyServer proxy) {
		
		this.frontend = new BungeeFrontend(netName);
		this.downstream = new DownstreamWrapper(proxy);
		
		this.map = new BungeeNetworkMap(this.frontend, this.downstream);
		
	}
	
	@Override
	public NetworkMap getLocalNetworkMap() {
		return this.map;
	}

	@Override
	public Set<NetworkMap> getGlobalNetworkMaps() {
		return new HashSet<>(Arrays.asList(this.map));
	}

}
