package io.neocore.manage.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

import io.neocore.api.NeocoreAPI;
import io.neocore.common.net.LockCoordinator;
import io.neocore.common.net.NetworkSync;
import io.neocore.manage.client.net.NmNetwork;
import io.neocore.manage.client.net.NmServer;
import io.neocore.manage.proto.ClientMessageUtils;
import io.neocore.manage.proto.NeomanageProtocol.ClientMessage;
import io.neocore.manage.proto.NeomanageProtocol.PlayerListUpdate;
import io.neocore.manage.proto.NeomanageProtocol.PlayerSubscriptionUpdate;
import io.neocore.manage.proto.NeomanageProtocol.PlayerUpdateNotification;
import io.neocore.manage.proto.NeomanageProtocol.UpdateBansNotification;
import io.neocore.manage.proto.NeomanageProtocol.UpdatePermissionsNotification;
import io.neocore.manage.proto.NeomanageProtocol.UpdatePermissionsNotification.ReloadExtent;

public class NmdNetworkSync extends NetworkSync {
	
	private UUID agentId;
	private NmNetwork network;
	
	private Consumer<UUID> invalidationCallback;
	
	public NmdNetworkSync(UUID agentId, NmNetwork net) {
		
		this.agentId = agentId;
		this.network = net;
		
	}
	
	private ClientMessage.Builder getBuilder() {
		return ClientMessageUtils.newBuilder(this.agentId);
	}
	
	@Override
	public void updateSubscriptionState(UUID uuid, boolean state) {
		
		ClientMessage.Builder b = this.getBuilder();
		b.setSubUpdate(PlayerSubscriptionUpdate.newBuilder().setUuid(uuid.toString()).setState(state));
		
		this.network.getActiveServer().queueMessage(b.build());
		
	}
	
	@Override
	public void updatePlayerList(Set<UUID> uuids) {

		ClientMessage.Builder b = this.getBuilder();
		
		List<String> uuidStrs = new ArrayList<>();
		uuids.forEach(id -> uuidStrs.add(id.toString()));
		b.setPlayerListUpdate(PlayerListUpdate.newBuilder().addAllUuids(uuidStrs));
		
		this.network.getActiveServer().queueMessage(b.build());
		
	}
	
	@Override
	public void announceInvalidation(UUID uuid) {
		
		NeocoreAPI.getLogger().fine("Announcing invalidation of " + uuid + "...");
		
		ClientMessage.Builder b = this.getBuilder();
		b.setUpdateNotification(
			PlayerUpdateNotification.newBuilder()
				.setPlayerId(uuid.toString())
				.build());
		
		this.network.getActiveServer().queueMessage(b.build());
		
	}
	
	@Override
	public void setInvalidationCallback(Consumer<UUID> callback) {
		
		NeocoreAPI.getLogger().fine("Invalidation callback installed.");
		this.invalidationCallback = callback;
		
	}
	
	public void handleInvalidation(UUID uuid) {
		if (this.invalidationCallback != null) this.invalidationCallback.accept(uuid);
	}
	
	@Override
	public LockCoordinator getLockCoordinator() {
		return new NmdLockCoordinator(this.agentId, this.network);
	}
	
	@Override
	public void announcePermissionsRefresh() {
		
		NmServer serv = this.network.getActiveServer();
		
		ClientMessage m =
			this.getBuilder()
				.setPermsUpdateNotification(UpdatePermissionsNotification.newBuilder()
					.setExtent(ReloadExtent.EVERYTHING))
				.build();
		
		serv.queueMessage(m);
		
	}
	
	@Override
	public void announceBansReload() {
		
		NmServer serv = this.network.getActiveServer();
		
		ClientMessage m =
			this.getBuilder()
				.setBansUpdateNotification(UpdateBansNotification.newBuilder())
				.build();
		
		serv.queueMessage(m);
		
	}
	
}
