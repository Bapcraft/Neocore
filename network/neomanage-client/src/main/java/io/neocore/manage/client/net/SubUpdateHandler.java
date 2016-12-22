package io.neocore.manage.client.net;

import java.util.UUID;

import io.neocore.manage.client.network.DaemonNetworkMapService;
import io.neocore.manage.proto.NeomanageProtocol.ClientMessage;
import io.neocore.manage.proto.NeomanageProtocol.PlayerSubscriptionUpdate;

public class SubUpdateHandler extends MessageHandler {
	
	private DaemonNetworkMapService mapService;
	
	public SubUpdateHandler(DaemonNetworkMapService service) {
		this.mapService = service;
	}
	
	@Override
	public void handle(NmServer sender, ClientMessage message) {
		
		PlayerSubscriptionUpdate psu = message.getSubUpdate();
		
		UUID senderAgentId = UUID.fromString(message.getSenderId());
		UUID playerId = UUID.fromString(psu.getUuid());
		
		// TODO
		
	}

}
