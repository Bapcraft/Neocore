package io.neocore.manage.server.handling;

import io.neocore.manage.proto.NeomanageProtocol.ClientMessage;
import io.neocore.manage.proto.NeomanageProtocol.UnregisterClient;
import io.neocore.manage.server.Nmd;
import io.neocore.manage.server.infrastructure.DaemonServer;
import io.neocore.manage.server.infrastructure.NmClient;

public class ClientDisconnectHandler extends MessageHandler {

	@Override
	public void handle(DaemonServer server, NmClient client, ClientMessage message) {
		
		UnregisterClient unreg = message.getUnregClient();
		
		Nmd.logger.fine("Disconnecting client " + client.getIdentString() + " for reason " + unreg.getReasonStr() + "...");
		server.unregister(client);
		
	}

}
