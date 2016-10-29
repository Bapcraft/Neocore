package io.neocore.manage.server.infrastructure;

import java.net.Socket;

public class ClientBuilder {
	
	private boolean built;
	
	private Socket socket;
	
	private String toNetwork, toName;
	
	public ClientBuilder(Socket sock) {
		this.socket = sock;
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
		
		NmClient cli = new NmClient(this.socket);
		
		cli.network = this.toNetwork;
		cli.name = this.toName;
		
		this.built = true;
		return cli;
		
	}
	
}
