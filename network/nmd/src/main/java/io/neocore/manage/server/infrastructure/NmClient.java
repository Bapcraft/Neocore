package io.neocore.manage.server.infrastructure;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

import io.neocore.manage.server.Nmd;

public class NmClient {
	
	protected Socket socket;
	
	protected String network;
	protected String name;
	
	private Thread listenerThread;
	private Set<UUID> subscriptions;
	
	public NmClient(Socket socket) {
		
		this.socket = socket;
		
		this.subscriptions = new HashSet<>();
		
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
		return (this.network != null ? this.network + "#" : "") + this.name;
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
		
		this.listenerThread = new Thread(new ClientListenRunner(this, serv), "ClientListenThread-" + this.getIdentString());
		this.listenerThread.start();
		
	}
	
	protected void forceDisconnect() {
		
		try {
			
			Nmd.logger.info("Forcibly closing connection to " + this.getIdentString() + "...");
			this.socket.close();
			
		} catch (IOException e) {
			Nmd.logger.log(Level.WARNING, "Problem closing connection to " + this.getIdentString() + "!", e);
		}
		
	}
	
}
