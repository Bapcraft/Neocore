package io.neocore.manage.server.handling;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.neocore.manage.proto.NeomanageProtocol.ClientMessage;
import io.neocore.manage.proto.NeomanageProtocol.PlayerListUpdate;
import io.neocore.manage.server.Nmd;
import io.neocore.manage.server.infrastructure.DaemonServer;
import io.neocore.manage.server.infrastructure.NmClient;

public class PlayerListUpdateHandler extends MessageHandler {

	@Override
	public void handle(DaemonServer server, NmClient client, ClientMessage message) {

		PlayerListUpdate plu = message.getPlayerListUpdate();

		Nmd.logger.finer("Updaing all " + plu.getUuidsCount() + " subs from " + client.getIdentString() + ".");

		List<UUID> uuids = new ArrayList<>(plu.getUuidsCount());
		plu.getUuidsList().forEach(u -> uuids.add(UUID.fromString(u)));
		client.updateSubscriptions(uuids);

	}

}
