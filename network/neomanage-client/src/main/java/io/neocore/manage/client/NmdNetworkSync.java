package io.neocore.manage.client;

import java.util.Set;
import java.util.UUID;

import io.neocore.common.player.LockCoordinator;
import io.neocore.common.player.NetworkSync;

public class NmdNetworkSync extends NetworkSync {
	
	private NmNetwork network;
	
	public NmdNetworkSync(NmNetwork net) {
		this.network = net;
	}
	
	@Override
	public void updateSubscriptionState(UUID uuid, boolean state) {
		this.network.broadcastUpdateSubscriptionState(uuid, state);
	}
	
	@Override
	public void updatePlayerList(Set<UUID> uuids) {
		this.network.broadcastUpdatePlayerList(uuids);
	}
	
	@Override
	public LockCoordinator getLockCoordinator() {
		return null;
	}
	
}
