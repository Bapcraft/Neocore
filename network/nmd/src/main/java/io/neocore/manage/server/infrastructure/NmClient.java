package io.neocore.manage.server.infrastructure;

import java.net.InetAddress;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class NmClient {
	
	private InetAddress address;
	private int port;
	
	protected String network;
	protected String name;
	
	private Set<UUID> subscriptions;
	
	public NmClient(InetAddress address, int port) {
		
		this.address = address;
		
		this.subscriptions = new HashSet<>();
		
	}
	
	public InetAddress getAddress() {
		return this.address;
	}
	
	public int getPort() {
		return this.port;
	}
	
	public String getAddressString() {
		return String.format("%s:%s", this.getAddress().getHostAddress(), this.getPort());
	}
	
	public void subscribe(UUID uuid) {
		this.subscriptions.add(uuid);
	}
	
	public void unsubscribe(UUID uuid) {
		this.subscriptions.remove(uuid);
	}
	
}
