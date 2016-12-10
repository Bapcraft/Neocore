package io.neocore.manage.client.net;

import io.neocore.api.NeocoreAPI;
import io.neocore.manage.proto.NeomanageProtocol.ClientMessage;

public class RemoteShutdownHandler extends MessageHandler {

	@Override
	public void handle(NmServer sender, ClientMessage message) {
		
		NeocoreAPI.getLogger().fine("Got message that remote server " + sender.getLabel() + " is shutting down...");
		sender.close();
		
	}

}
