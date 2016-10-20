package io.neocore.manage.client;

import java.util.List;

public class NmNetwork {
	
	public List<NmServer> servers;
	
	public NmNetwork(List<NmServer> servs) {
		this.servers = servs;
	}
	
	public NmServer getActiveServer() {
		
		for (NmServer serv : this.servers) {
			if (serv.isOnline()) return serv;
		}
		
		throw new IllegalStateException("No Neomanage servers active!");
		
	}
	
}
