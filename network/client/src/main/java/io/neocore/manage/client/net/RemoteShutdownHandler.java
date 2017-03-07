package io.neocore.manage.client.net;

import io.neocore.api.NeocoreAPI;
import io.neocore.manage.proto.NeomanageProtocol.ClientMessage;

public class RemoteShutdownHandler extends MessageHandler {

	private NmNetwork network;

	public RemoteShutdownHandler(NmNetwork net) {
		this.network = net;
	}

	@Override
	public void handle(NmServer sender, ClientMessage message) {

		NeocoreAPI.getLogger().info("Got message that remote server " + sender.getLabel() + " is shutting down...");

		sender.forceClose();
		this.network.removeServer(sender);

	}

}
