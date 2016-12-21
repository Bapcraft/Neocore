package io.neocore.manage.server.handling;

import java.util.UUID;

import io.neocore.manage.proto.NeomanageProtocol.ClientMessage;
import io.neocore.manage.server.infrastructure.DaemonServer;
import io.neocore.manage.server.infrastructure.NmClient;

public class PlayerUpdateHandler extends MessageHandler {
	
	private boolean ignoreSubscriptions;
	
	public PlayerUpdateHandler() {
		
		this.ignoreSubscriptions = Boolean.parseBoolean(System.getProperty("io.neocore.manage.server.IgnoreClientSubsciptions", "false"));
		
	}
	
	@Override
	public void handle(DaemonServer server, NmClient client, ClientMessage message) {
		
		UUID playerId = UUID.fromString(message.getPlayerUpdate().getPlayerId());
		
		for (NmClient cli : server.getClients()) {
			
			if ((!ignoreSubscriptions && cli.isSubscribed(playerId)) && cli != client) {
				cli.queueMessage(message);
			}
			
		}
		
	}

}
