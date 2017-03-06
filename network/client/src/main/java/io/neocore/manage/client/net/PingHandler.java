package io.neocore.manage.client.net;

import io.neocore.api.NeocoreAPI;
import io.neocore.manage.proto.NeomanageProtocol.ClientMessage;

public class PingHandler extends MessageHandler {

	@Override
	public void handle(NmServer sender, ClientMessage message) {
		NeocoreAPI.getLogger().finest("Got ping back from " + sender.getLabel() + ".");
	}

}
