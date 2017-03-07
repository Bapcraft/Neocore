package io.neocore.manage.client.net;

import java.util.UUID;

import io.neocore.api.NeocoreAPI;
import io.neocore.manage.client.NmdNetworkSync;
import io.neocore.manage.proto.NeomanageProtocol.ClientMessage;
import io.neocore.manage.proto.NeomanageProtocol.PlayerUpdateNotification;

public class PlayerUpdateHandler extends MessageHandler {

	private NmdNetworkSync sync;

	public PlayerUpdateHandler(NmdNetworkSync sync) {
		this.sync = sync;
	}

	@Override
	public void handle(NmServer sender, ClientMessage message) {

		PlayerUpdateNotification pun = message.getUpdateNotification();
		UUID uuid = UUID.fromString(pun.getPlayerId());

		NeocoreAPI.getLogger()
				.finer("Got invalidation notification from " + message.getSenderId() + " for player " + uuid + ".");
		this.sync.handleInvalidation(uuid);

	}

}
