package io.neocore.manage.server.infrastructure;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import io.neocore.manage.proto.NeomanageProtocol.RegisterClient;
import io.neocore.manage.proto.NeomanageProtocol.RegisterClient.ClientType;
import io.neocore.manage.server.ClientAcceptWorker;
import io.neocore.manage.server.Nmd;
import io.neocore.manage.server.Scheduler;
import io.neocore.manage.proto.NeomanageProtocol.ServerClient;

public class DaemonServer {
	
	private Scheduler scheduler;
	private ServerSocket socket;
	
	private List<ClientAcceptWorker> acceptors;
	private List<Thread> acceptorThreads;
	
	private List<NmClient> clients;
	
	public DaemonServer(Scheduler sched, ServerSocket socket, int threadCount) {
		
		this.scheduler = sched;
		this.socket = socket;
		
		this.acceptors = new ArrayList<>();
		this.acceptorThreads = new ArrayList<>();
		
		this.clients = new ArrayList<>();
		
		for (int i = 0; i < threadCount; i++) {
			
			ClientAcceptWorker proc = new ClientAcceptWorker(this.socket, s -> this.processConnection(s));
			Thread t = new Thread(proc, "ConnectionAcceptor-" + i);
			
			this.acceptors.add(proc);
			this.acceptorThreads.add(t);
			
		}
		
	}
	
	public void startListening() {
		this.acceptorThreads.forEach(t -> t.start());
	}
	
	private void processConnection(Socket socket) {
		
		this.scheduler.invokeAsync(
			
			String.format("InitClient(%s:%s)", socket.getInetAddress().getHostAddress(), socket.getPort()),
			
			() -> {
				
				ClientBuilder builder = new ClientBuilder(socket);
				
				try {
					
					RegisterClient reg = RegisterClient.parseFrom(socket.getInputStream());
					
					ClientType type = reg.getClientType();
					
					// FIXME Use inheritance.
					if (type == ClientType.SERVER) {
						
						ServerClient cli = reg.getClientServer();
						
						builder.withName(cli.getServerName());
						builder.withNetwork(cli.getProxyName());
						
						this.clients.add(builder.build());
						
					}
					
				} catch (IOException e) {
					Nmd.logger.log(Level.WARNING, "Problem initializing client!", e);
				}
				
			}
			
		);
		
	}
	
	public List<NmClient> getClients() {
		return new ArrayList<>(this.clients);
	}
	
	public void unregister(NmClient client) {
		
		if (this.clients.contains(client)) {
			
			client.forceDisconnect();
			this.clients.remove(client);
			Nmd.logger.info("Client " + client.getIdentString() + " from " + client.getAddressString() + " disconnected.");
			
		} else {
			throw new IllegalArgumentException("Could not find client " + client.getIdentString() + ".  Is it already unregistered?");
		}
		
	}
	
}
