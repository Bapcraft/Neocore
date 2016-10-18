package io.neocore.api.database.session;

import java.net.InetAddress;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import io.neocore.api.host.proxy.NetworkEndpoint;

/**
 * A player's session withing a proxied network.
 * 
 * @author treyzania
 */
public class ProxiedSession extends Session {
	
	protected List<EndpointMove> moves;
	
	public ProxiedSession(UUID uuid, String name, InetAddress src, String inbound, List<EndpointMove> moves) {
		
		super(uuid, name, src, inbound);
		
		this.moves = moves;
		
	}
	
	/**
	 * @return A list of moves between endpoints on the server.
	 */
	public List<EndpointMove> getMoves() {
		return Collections.unmodifiableList(this.moves);
	}
	
	/**
	 * @return The first endpoint that the player connected to.
	 */
	public NetworkEndpoint getInitialEndpoint() {
		return this.getMoves().get(0).destination;
	}

	@Override
	public boolean isNetwork() {
		return true;
	}
	
}
