package io.neocore.manage.server.handling;

import io.neocore.manage.proto.NeomanageProtocol.ClientMessage;
import io.neocore.manage.server.Nmd;
import io.neocore.manage.server.infrastructure.DaemonServer;
import io.neocore.manage.server.infrastructure.NmClient;

public class UnsupportedHandler extends MessageHandler {

	@Override
	public void handle(DaemonServer server, NmClient client, ClientMessage message) {
		
		Nmd.logger.warning("Got unsupported message from client " + client.getIdentString() + "! (" + client.getAddressString() + ")");
		
	}

}
