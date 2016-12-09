package io.neocore.manage.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import io.neocore.api.NeocoreAPI;
import io.neocore.common.player.LockCoordinator;
import io.neocore.common.player.NetworkSync;
import io.neocore.manage.proto.ClientMessageUtils;
import io.neocore.manage.proto.NeomanageProtocol.ClientMessage;
import io.neocore.manage.proto.NeomanageProtocol.PlayerListUpdate;
import io.neocore.manage.proto.NeomanageProtocol.PlayerSubscriptionUpdate;

public class NmdNetworkSync extends NetworkSync {
	
	private NmNetwork network;
	
	public NmdNetworkSync(NmNetwork net) {
		this.network = net;
	}
	
	@Override
	public void updateSubscriptionState(UUID uuid, boolean state) {
		
		ClientMessage.Builder b = ClientMessageUtils.newBuilder(NeocoreAPI.getAgent().getAgentId());
		b.setSubUpdate(PlayerSubscriptionUpdate.newBuilder().setUuid(uuid.toString()).setState(state));
		
		this.network.getActiveServer().queueMessage(b.build());
		
	}
	
	@Override
	public void updatePlayerList(Set<UUID> uuids) {

		ClientMessage.Builder b = ClientMessageUtils.newBuilder(NeocoreAPI.getAgent().getAgentId());
		
		List<String> uuidStrs = new ArrayList<>();
		uuids.forEach(id -> uuidStrs.add(id.toString()));
		b.setPlayerListUpdate(PlayerListUpdate.newBuilder().addAllUuids(uuidStrs));
		
		this.network.getActiveServer().queueMessage(b.build());
		
	}
	
	@Override
	public LockCoordinator getLockCoordinator() {
		return null;
	}
	
}
