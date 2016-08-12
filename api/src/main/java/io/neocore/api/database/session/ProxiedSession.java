package io.neocore.api.database.session;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.neocore.api.host.HostContext;
import io.neocore.api.host.proxy.NetworkEndpoint;

public class ProxiedSession extends Session {
	
	private List<EndpointMove> moves;
	
	public ProxiedSession(UUID uuid, String name, InetAddress src, HostContext context) {
		
		super(uuid, name, src, context);
		
		this.moves = new ArrayList<>();
		
	}
	
	public List<EndpointMove> getMoves() {
		return this.moves;
	}
	
	public NetworkEndpoint getInitialEndpoint() {
		return this.getMoves().get(0).destination;
	}
	
}
