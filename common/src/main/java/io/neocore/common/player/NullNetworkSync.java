package io.neocore.common.player;

import java.util.Set;
import java.util.UUID;

public class NullNetworkSync extends NetworkSync {
	
	@Override
	public void updateSubscriptionState(UUID uuid, boolean state) {
		// Nothing
	}

	@Override
	public void updatePlayerList(Set<UUID> uuids) {
		// Nothing
	}

	@Override
	public LockCoordinator getLockCoordinator() {
		return new NullLockCoordinator();
	}

}
