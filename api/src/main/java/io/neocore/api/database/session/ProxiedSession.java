package io.neocore.api.database.session;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.neocore.api.host.HostContext;
import io.neocore.api.host.proxy.NetworkEndpoint;

/**
 * A player's session withing a proxied network.
 * 
 * @author treyzania
 */
public class ProxiedSession extends Session {
	
	private List<EndpointMove> moves;
	
	public ProxiedSession(UUID uuid, String name, InetAddress src, HostContext context) {
		
		super(uuid, name, src, context);
		
		this.moves = new ArrayList<>();
		
	}
	
	/**
	 * @return A list of moves between endpoints on the server.
	 */
	public List<EndpointMove> getMoves() {
		return this.moves;
	}
	
	/**
	 * @return The first endpoint that the player connected to.
	 */
	public NetworkEndpoint getInitialEndpoint() {
		return this.getMoves().get(0).destination;
	}
	
}
