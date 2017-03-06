package io.neocore.manage.server.handling;

import io.neocore.manage.proto.NeomanageProtocol.ClientMessage;
import io.neocore.manage.server.infrastructure.DaemonServer;
import io.neocore.manage.server.infrastructure.NmClient;

public abstract class MessageHandler {
	
	public MessageHandler() {
		
	}
	
	public abstract void handle(DaemonServer server, NmClient client, ClientMessage message);
	
}
