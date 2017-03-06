package io.neocore.manage.server.handling;

import java.util.UUID;

import io.neocore.manage.proto.NeomanageProtocol.ClientMessage;
import io.neocore.manage.proto.NeomanageProtocol.PlayerSubscriptionUpdate;
import io.neocore.manage.server.Nmd;
import io.neocore.manage.server.infrastructure.DaemonServer;
import io.neocore.manage.server.infrastructure.NmClient;

public class SubUpdateHandler extends MessageHandler {

	@Override
	public void handle(DaemonServer server, NmClient client, ClientMessage message) {
		
		PlayerSubscriptionUpdate psu = message.getSubUpdate();
		
		UUID uuid = UUID.fromString(psu.getUuid());
		Nmd.logger.finer("Setting sub state of " + client.getIdentString() + " to " + uuid + " to " + psu.getState());
		
		if (psu.getState()) {
			client.subscribe(uuid);
		} else {
			client.unsubscribe(uuid);
		}
		
	}

}
