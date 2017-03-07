package io.neocore.manage.server.handling;

import io.neocore.manage.proto.NeomanageProtocol.ClientMessage;
import io.neocore.manage.server.Nmd;
import io.neocore.manage.server.infrastructure.DaemonServer;
import io.neocore.manage.server.infrastructure.NmClient;

public class RebroadcastHandler extends MessageHandler {

	@Override
	public void handle(DaemonServer server, NmClient client, ClientMessage message) {

		Nmd.logger.fine("Relaying message of type " + message.getPayloadCase().name() + " to clients.");
		server.queueToAll(message);

	}

}
