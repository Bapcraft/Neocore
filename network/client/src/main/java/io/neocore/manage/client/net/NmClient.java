package io.neocore.manage.client.net;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.UUID;

import io.neocore.api.Neocore;
import io.neocore.manage.proto.NeomanageProtocol.ClientHandshake;
import io.neocore.manage.proto.NeomanageProtocol.ClientHandshake.ClientType;
import io.neocore.manage.proto.NeomanageProtocol.ServerClient;
import io.neocore.manage.proto.NeomanageProtocol.ServerClient.ServerRole;
import io.neocore.manage.proto.NeomanageProtocol.ServerClient.ServerType;

public class NmClient {
	
	private Neocore agent;
	private HandlerManager handlers;
	
	private ServerType type;
	private ServerRole role;
	
	public NmClient(Neocore agent, HandlerManager hanMan, ServerType type, ServerRole role) {
		
		this.agent = agent;
		this.handlers = hanMan;
		
		this.type = type;
		this.role = role;
		
	}
	
	public NmServer connect(InetSocketAddress addr, int timeout, long msgTimeout) throws IOException, SocketTimeoutException {
		
		// Create the socket and connect.
		Socket socket = new Socket();
		socket.connect(addr, timeout);
		
		// Write the handshake.
		OutputStream os = socket.getOutputStream();
		this.getHandshake().writeDelimitedTo(os);
		os.flush();
		
		// Return the newly-created server.
		return new NmServer(socket, msgTimeout, this.handlers);
		
	}
	
	public ClientHandshake getHandshake() {
		
		// Builders.
		ClientHandshake.Builder hsb = ClientHandshake.newBuilder();
		ServerClient.Builder scb = ServerClient.newBuilder();
		
		// Set up the handshake information itself.
		hsb.setAgentId(this.agent.getAgentId().toString());
		hsb.setClientType(ClientType.SERVER);
		
		// The server client metadata.
		scb.setServerName(this.agent.getServerName());
		scb.setNetworkName(this.agent.isNetworked() ? this.getNetworkName() : "~");
		scb.setServerType(this.type);
		scb.setServerRole(this.role);
		hsb.setServerClient(scb.build());
		
		return hsb.build();
		
	}
	
	public UUID getAgentId() {
		return this.agent.getAgentId();
	}
	
	public String getServerName() {
		return this.agent.getServerName();
	}
	
	public String getNetworkName() {
		return this.agent.getNetworkName();
	}
	
	public ServerType getType() {
		return this.type;
	}
	
	public ServerRole getRole() {
		return this.role;
	}
	
}
