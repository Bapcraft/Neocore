package io.neocore.manage.client.net;

import java.util.UUID;

import io.neocore.manage.client.network.NmNetworkMapService;
import io.neocore.api.NeocoreAPI;
import io.neocore.manage.client.network.NmNetworkComponent;
import io.neocore.manage.client.network.NmNetworkMap;
import io.neocore.manage.proto.NeomanageProtocol.ClientMessage;
import io.neocore.manage.proto.NeomanageProtocol.PlayerSubscriptionUpdate;

public class SubUpdateHandler extends MessageHandler {
	
	private NmNetworkMapService mapService;
	
	public SubUpdateHandler(NmNetworkMapService service) {
		this.mapService = service;
	}
	
	@Override
	public void handle(NmServer sender, ClientMessage message) {
		
		PlayerSubscriptionUpdate psu = message.getSubUpdate();
		
		UUID senderAgentId = UUID.fromString(message.getSenderId());
		UUID playerId = UUID.fromString(psu.getUuid());
		boolean state = psu.getState();
		
		NmNetworkMap map = this.mapService.getNetworkByMemberId(senderAgentId);
		NmNetworkComponent comp = this.mapService.getComponentById(senderAgentId);
		
		if (map == null) {
			
			NeocoreAPI.getLogger().warning("No network map found containing senderId: " + senderAgentId + " (Ignoring packet.)");
			return;
			
		}
		
		if (comp == null) {
			
			NeocoreAPI.getLogger().warning("No component for senderId: " + senderAgentId + " (Ignoring packet.)");
			return;
			
		}
		
		if (state) {
			map.addPlayerTo(playerId, comp);
		} else {
			map.removePlayerFrom(playerId, comp);
		}
		
	}
	
}
