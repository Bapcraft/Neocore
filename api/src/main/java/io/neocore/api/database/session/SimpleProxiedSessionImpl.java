package io.neocore.api.database.session;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import io.neocore.api.host.proxy.NetworkEndpoint;

/**
 * A player's session within a proxied network.
 * 
 * @author treyzania
 */
public class SimpleProxiedSessionImpl extends SimpleSessionImpl implements ProxiedSession {
	
	protected List<EndpointMoveImpl> moves;
	
	public SimpleProxiedSessionImpl(UUID uuid, String name, InetAddress src, String inbound, List<EndpointMoveImpl> moves) {
		
		super(uuid, name, src, inbound);
		
		this.moves = moves;
		
	}
	
	/**
	 * @return A list of moves between endpoints on the server.
	 */
	public List<EndpointMoveImpl> getMoves() {
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

	@Override
	public List<EndpointMove> getEndpointMoves() {
		
		List<EndpointMove> ret = new ArrayList<>();
		this.moves.forEach(r -> ret.add(r));
		return ret;
		
	}

	@Override
	public void addEndpointMove(EndpointMove move) {
		// FIXME
	}
	
}
