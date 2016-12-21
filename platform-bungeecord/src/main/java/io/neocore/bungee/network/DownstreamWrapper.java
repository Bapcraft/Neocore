package io.neocore.bungee.network;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

/**
 * Simple wrapper for managing endoint creation, etc.
 * 
 * TODO Make this have a local cache so we don't keep creating the endpoints,
 * and hook up a system to clear the cache of unneeded endpoints periodically.
 * 
 * @author treyzania
 */
public class DownstreamWrapper {
	
	private ProxyServer bungee;
	
	public DownstreamWrapper(ProxyServer serv) {
		this.bungee = serv;
	}
	
	/**
	 * @return A set of all of the endpoints at the time this was called.
	 */
	public Set<BungeeDownstreamEndpoint> getEndpoints() {
		
		Set<BungeeDownstreamEndpoint> eps = new HashSet<>();
		for (Map.Entry<String, ServerInfo> entry : this.bungee.getServers().entrySet()) {
			eps.add(new BungeeDownstreamEndpoint(UUID.randomUUID(), entry.getKey(), entry.getValue()));
		}
		
		return eps;
		
	}
	
	/**
	 * Returns an applicable instance of the specified network endpoint for the
	 * given name.  Can return null for invalid endpoints.
	 * 
	 * @param name The name of the endpoint.
	 * @return The endpoint itself.
	 */
	public BungeeDownstreamEndpoint getEndpoint(String name) {
		
		for (BungeeDownstreamEndpoint ep : this.getEndpoints()) {
			if (ep.getAgentName().equals(name)) return ep;
		}
		
		return null;
		
	}
	
}
