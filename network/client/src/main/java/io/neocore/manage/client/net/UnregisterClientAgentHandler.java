package io.neocore.manage.client.net;

import java.util.UUID;

import io.neocore.api.NeocoreAPI;
import io.neocore.manage.client.network.NmNetworkMapService;
import io.neocore.manage.proto.NeomanageProtocol.ClientMessage;
import io.neocore.manage.proto.NeomanageProtocol.UnregisterClient;

public class UnregisterClientAgentHandler extends MessageHandler {

	private NmNetworkMapService service;

	public UnregisterClientAgentHandler(NmNetworkMapService serv) {
		this.service = serv;
	}

	@Override
	public void handle(NmServer sender, ClientMessage message) {

		UnregisterClient uc = message.getUnregClient();

		UUID unregisteringId = UUID.fromString(message.getSenderId());
		NeocoreAPI.getLogger().fine("Got unregistration for " + unregisteringId + " of type " + uc.getReason().name()
				+ ", specifically \"" + uc.getReasonStr() + "\".");
		this.service.unregisterClient(unregisteringId);

	}

}
