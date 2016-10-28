package io.neocore.manage.server.infrastructure;

import java.net.InetAddress;

public class ClientBuilder {
	
	private boolean built;
	
	private InetAddress address;
	private int port;
	
	private String toNetwork, toName;
	
	public ClientBuilder(InetAddress addr, int port) {
		
		this.address = addr;
		this.port = port;
		
	}
	
	public ClientBuilder withNetwork(String net) {
		
		this.toNetwork = net;
		return this;
		
	}
	
	public ClientBuilder withName(String name) {
		
		this.toName = name;
		return this;
		
	}
	
	public synchronized NmClient build() {
		
		if (this.built) throw new IllegalStateException("Client already built!");
		
		NmClient cli = new NmClient(this.address, this.port);
		
		cli.network = this.toNetwork;
		cli.name = this.toName;
		
		this.built = true;
		return cli;
		
	}
	
}
