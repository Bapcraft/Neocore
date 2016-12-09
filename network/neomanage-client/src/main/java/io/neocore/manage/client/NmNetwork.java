package io.neocore.manage.client;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import io.neocore.manage.proto.NeomanageProtocol.ClientMessage;

public class NmNetwork {
	
	public List<NmServer> servers;
	
	private Queue<ClientMessage> sendQueue = new LinkedBlockingQueue<>();
	
	public NmNetwork(List<NmServer> servs) {
		this.servers = servs;
	}
	
	public NmServer getActiveServer() {
		
		for (NmServer serv : this.servers) {
			if (serv.isOnline()) return serv;
		}
		
		throw new IllegalStateException("No Neomanage servers active!");
		
	}
	
	public void queueMessage(ClientMessage message) {
		this.sendQueue.add(message);
	}
	
}
