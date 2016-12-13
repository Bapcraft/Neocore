package io.neocore.manage.server.infrastructure;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;

import io.neocore.manage.proto.ClientMessageUtils;
import io.neocore.manage.proto.NeomanageProtocol.ClientMessage;
import io.neocore.manage.proto.NeomanageProtocol.DaemonShutdown;
import io.neocore.manage.server.Nmd;

public class NmClient {
	
	private DaemonServer server;
	
	protected Socket socket;
	private Queue<ClientMessage> sendQueue = new LinkedBlockingQueue<>();
	
	protected String network;
	protected String name;
	
	private ClientListenRunner listenerRunner;
	private Thread listenerThread;
	private Set<UUID> subscriptions = new HashSet<>();
	
	private long lastMessageTime;
	
	public NmClient(Socket socket, DaemonServer server) {
		
		this.socket = socket;
		this.server = server;
		
		this.lastMessageTime = System.currentTimeMillis();
		
	}
	
	public InetAddress getAddress() {
		return this.socket.getInetAddress();
	}
	
	public int getPort() {
		return this.socket.getPort();
	}
	
	public String getAddressString() {
		return String.format("%s:%s", this.getAddress().getHostAddress(), this.getPort());
	}
	
	public String getIdentString() {
		return (this.network != null ? this.network + "." : "") + this.name;
	}
	
	public void subscribe(UUID uuid) {
		this.subscriptions.add(uuid);
	}
	
	public void unsubscribe(UUID uuid) {
		this.subscriptions.remove(uuid);
	}
	
	public boolean isSubscribed(UUID uuid) {
		return this.subscriptions.contains(uuid);
	}
	
	public synchronized void startListenerThread(DaemonServer serv) {
		
		if (this.listenerThread != null) throw new IllegalStateException("Already listening!");
		
		this.listenerRunner = new ClientListenRunner(this, serv);
		this.listenerThread = new Thread(this.listenerRunner, "ClientListenThread-" + this.getIdentString());
		this.listenerThread.start();
		
	}
	
	protected synchronized void forceDisconnect() {
		
		if (this.socket.isClosed()) return;
		
		try {
			
			Nmd.logger.info("Forcibly closing connection to " + this.getIdentString() + "...");
			this.socket.close();
			
		} catch (IOException e) {
			Nmd.logger.log(Level.WARNING, "Problem closing connection to " + this.getIdentString() + "!", e);
		}
		
		this.server.remove(this);
		
	}
	
	public synchronized void disconnect() {
		
		if (this.socket.isClosed()) return;
		
		this.listenerRunner.disable();
		
		try {
			
			OutputStream os = this.socket.getOutputStream();
			ClientMessage.Builder b = ClientMessageUtils.newBuilder(new UUID(0, 0));
			b
				.setDaemonShutdown(
						DaemonShutdown
						.newBuilder()
						.setReason("tell treyzania to fix this"))
				.build()
				.writeDelimitedTo(os);
			os.flush();
			
			this.socket.close();
			
		} catch (IOException e) {
			Nmd.logger.log(Level.WARNING, "Problem when closing connection.", e);
		}
		
		this.server.remove(this);
		
	}
	
	public void update() {
		this.lastMessageTime = System.currentTimeMillis();
	}
	
	public long getLastUpdated() {
		return this.lastMessageTime;
	}
	
	public void queueMessage(ClientMessage msg) {
		this.sendQueue.add(msg);
	}

	public boolean isConnected() {
		return !this.socket.isClosed();
	}
	
}
