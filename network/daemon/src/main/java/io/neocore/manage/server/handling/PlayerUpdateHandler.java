package io.neocore.manage.server.handling;

import java.util.UUID;

import io.neocore.manage.proto.NeomanageProtocol.ClientMessage;
import io.neocore.manage.server.Nmd;
import io.neocore.manage.server.infrastructure.DaemonServer;
import io.neocore.manage.server.infrastructure.NmClient;

public class PlayerUpdateHandler extends MessageHandler {
	
	private boolean ignoreSubscriptions;
	
	public PlayerUpdateHandler() {
		
		this.ignoreSubscriptions = Boolean.parseBoolean(System.getProperty("io.neocore.manage.server.IgnoreClientSubscriptions", "false"));
		
		if (this.ignoreSubscriptions) Nmd.logger.info("PlayerUpdateHandler set to ignore subscriptions.");
		
	}
	
	@Override
	public void handle(DaemonServer server, NmClient client, ClientMessage message) {
		
		UUID playerId = UUID.fromString(message.getUpdateNotification().getPlayerId());
		
		for (NmClient cli : server.getClients()) {
			
			if ((ignoreSubscriptions || cli.isSubscribed(playerId)) && cli != client) {
				
				Nmd.logger.fine("Sending invalidation to " + cli.getIdentString() + "...");
				cli.queueMessage(message);
				
			}
			
		}
		
	}

}
