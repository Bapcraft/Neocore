package io.neocore.manage.client.net;

import java.util.UUID;
import java.util.logging.Logger;

import io.neocore.api.NeocoreAPI;
import io.neocore.manage.proto.ClientMessageUtils;
import io.neocore.manage.proto.NeomanageProtocol.ClientMessage;
import io.neocore.manage.proto.NeomanageProtocol.Ping;

public class PingRunner implements Runnable {
	
	private UUID agentId;
	
	private NmNetwork network;
	private long period;
	
	public PingRunner(UUID agentId, NmNetwork net, long period) {
		
		this.agentId = agentId;
		this.network = net;
		this.period = period;
		
	}
	
	@Override
	public void run() {
		
		Logger log = NeocoreAPI.getLogger();
		
		while (true) {
			
			for (NmServer serv : this.network.servers) {
				
				ClientMessage msg =
					ClientMessageUtils
						.newBuilder(this.agentId)
						.setPing(Ping.getDefaultInstance())
						.build();
				
				log.finer("Pinging to " + serv.getLabel() + "...");
				serv.queueMessage(msg);
				
			}
			
			try {
				Thread.sleep(this.period);
			} catch (InterruptedException e) {
				log.warning("Sleep ping thread interrupted, resuming immediately.");
			}
			
		}
		
	}

}
