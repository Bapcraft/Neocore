package io.neocore.manage.server.infrastructure;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import io.neocore.manage.proto.NeomanageProtocol.ClientHandshake;
import io.neocore.manage.proto.NeomanageProtocol.ClientHandshake.ClientType;
import io.neocore.manage.proto.NeomanageProtocol.ClientMessage;
import io.neocore.manage.proto.NeomanageProtocol.ClientMessage.PayloadCase;
import io.neocore.manage.server.ClientAcceptWorker;
import io.neocore.manage.server.Nmd;
import io.neocore.manage.server.Scheduler;
import io.neocore.manage.server.handling.ClientDisconnectHandler;
import io.neocore.manage.server.handling.RebroadcastHandler;
import io.neocore.manage.proto.NeomanageProtocol.ServerClient;

public class DaemonServer {
	
	private Scheduler scheduler;
	private ServerSocket socket;
	
	private List<ClientAcceptWorker> acceptors;
	private List<Thread> acceptorThreads;
	
	private List<NmClient> clients;
	
	private MessageManager messageManager;
	
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
		
		this.messageManager = new MessageManager();
		this.messageManager.registerHandler(PayloadCase.UNREGCLIENT, new ClientDisconnectHandler());
		this.messageManager.registerHandler(PayloadCase.SUBUPDATE, new RebroadcastHandler());
		this.messageManager.registerHandler(PayloadCase.PLAYERLISTUPDATE, new RebroadcastHandler());
		
	}
	
	public void startListening() {
		this.acceptorThreads.forEach(t -> t.start());
	}
	
	private void processConnection(Socket socket) {
		
		this.scheduler.invokeAsync(
			
			String.format("InitClient(%s:%s)", socket.getInetAddress().getHostAddress(), socket.getPort()),
			
			() -> {
				
				ClientBuilder builder = new ClientBuilder(this, socket);
				
				try {
					
					Nmd.logger.fine("Awaiting handshake for " + socket.getInetAddress().getHostAddress() + "...");
					ClientHandshake reg = ClientHandshake.parseDelimitedFrom(socket.getInputStream());
					
					ClientType type = reg.getClientType();
					
					// FIXME Use inheritance.
					if (type == ClientType.SERVER) {
						
						ServerClient cli = reg.getServerClient();
						
						builder.withName(cli.getServerName());
						builder.withNetwork(cli.getNetworkName());
						
						NmClient nmc = builder.build();
						this.register(nmc);
						
						Nmd.logger.info("New client " + nmc.name + " connected from " + nmc.socket.getInetAddress().getHostAddress() + ".");
						
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
	
	private void register(NmClient client) {
		
		if (this.clients.contains(client)) throw new IllegalArgumentException("Already have this client registered!");
		
		this.clients.add(client);
		client.startIoThreads();
		
	}
	
	public void unregister(NmClient client) {
		
		if (this.clients.contains(client)) {
			
			client.disconnect();
			this.remove(client);
			Nmd.logger.info("Client " + client.getIdentString() + " from " + client.getAddressString() + " disconnected.");
			
		} else {
			throw new IllegalArgumentException("Could not find client " + client.getIdentString() + ".  Is it already unregistered?");
		}
		
	}
	
	public boolean remove(NmClient client) {
		return this.clients.remove(client);
	}
	
	public void removeDeadConnections() {
		this.clients.removeIf(c -> !c.isConnected());
	}
	
	public MessageManager getMessageManager() {
		return this.messageManager;
	}
	
	public void queueToAll(ClientMessage message) {
		
		for (NmClient cli : this.clients) {
			cli.queueMessage(message);
		}
		
	}
	
}
