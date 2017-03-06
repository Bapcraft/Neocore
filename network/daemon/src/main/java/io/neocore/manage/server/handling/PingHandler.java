package io.neocore.manage.server.handling;

import io.neocore.manage.proto.ClientMessageUtils;
import io.neocore.manage.proto.NeomanageProtocol.ClientMessage;
import io.neocore.manage.proto.NeomanageProtocol.Ping;
import io.neocore.manage.server.Nmd;
import io.neocore.manage.server.infrastructure.DaemonServer;
import io.neocore.manage.server.infrastructure.NmClient;

public class PingHandler extends MessageHandler {

	@Override
	public void handle(DaemonServer server, NmClient client, ClientMessage message) {
		
		Nmd.logger.finer("Got ping from " + client.getIdentString() + ".");
		
		ClientMessage.Builder b = ClientMessage.newBuilder();
		b.setMessageId(ClientMessageUtils.random());
		b.setPing(Ping.newBuilder().build());
		
		client.queueMessage(b.build());
		
	}

}
