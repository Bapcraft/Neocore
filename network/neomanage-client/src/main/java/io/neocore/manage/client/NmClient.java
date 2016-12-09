package io.neocore.manage.client;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.UUID;

import io.neocore.api.Neocore;
import io.neocore.manage.proto.NeomanageProtocol.RegisterClient;
import io.neocore.manage.proto.NeomanageProtocol.RegisterClient.ClientType;
import io.neocore.manage.proto.NeomanageProtocol.ServerClient;
import io.neocore.manage.proto.NeomanageProtocol.ServerClient.ServerRole;
import io.neocore.manage.proto.NeomanageProtocol.ServerClient.ServerType;

public class NmClient {
	
	private Neocore agent;
	private String name, network;
	
	private ServerType type;
	private ServerRole role;
	
	public NmClient(Neocore agent, String name, String network, ServerType type, ServerRole role) {
		
		this.agent = agent;
		
		this.name = name;
		this.network = network;
		
		this.type = type;
		this.role = role;
		
	}
	
	public NmServer connect(InetSocketAddress addr, int timeout) throws IOException, SocketTimeoutException {
		
		// Create the socket and connect.
		Socket socket = new Socket();
		socket.connect(addr, timeout);
		
		// Write the handshake.
		OutputStream os = socket.getOutputStream();
		os.write(this.getHandshake().toByteArray());
		
		// Return the newly-created server.
		return new NmServer(socket);
		
	}
	
	public RegisterClient getHandshake() {
		
		// Builders.
		RegisterClient.Builder rcb = RegisterClient.newBuilder();
		ServerClient.Builder scb = ServerClient.newBuilder();
		
		// Set up the handshake information itself.
		rcb.setAgentId(this.agent.getAgentId().toString());
		rcb.setClientType(ClientType.SERVER);
		
		// The server client metadata.
		scb.setServerName(this.name);
		if (this.network != null) scb.setNetworkName(this.network);
		scb.setServerType(this.type);
		scb.setServerRole(this.role);
		rcb.setServerClient(scb.build());
		
		return rcb.build();
		
	}
	
	public UUID getAgentId() {
		return this.agent.getAgentId();
	}
	
	public String getServerName() {
		return this.name;
	}
	
	public String getNetworkName() {
		return this.network;
	}
	
	public ServerType getType() {
		return this.type;
	}
	
	public ServerRole getRole() {
		return this.role;
	}
	
}
