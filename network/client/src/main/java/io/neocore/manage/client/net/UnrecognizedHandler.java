package io.neocore.manage.client.net;

import io.neocore.api.NeocoreAPI;
import io.neocore.manage.proto.NeomanageProtocol.ClientMessage;

public class UnrecognizedHandler extends MessageHandler {

	@Override
	public void handle(NmServer sender, ClientMessage message) {

		// Just report it and do nothing with it.
		NeocoreAPI.getLogger().warning(
				"Received message from " + sender.getLabel() + " of unknown type " + message.getPayloadCase() + "!");

	}

}
