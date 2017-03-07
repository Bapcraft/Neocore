package io.neocore.common.net;

import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

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
	public void announceInvalidation(UUID uuid) {
		// Nothing
	}

	@Override
	public void setInvalidationCallback(Consumer<UUID> callback) {
		// Nothing
	}

	@Override
	public LockCoordinator getLockCoordinator() {
		return new NullLockCoordinator();
	}

	@Override
	public void announcePermissionsRefresh() {
		// Nothing
	}

	@Override
	public void announceBansReload() {
		// Nothing
	}

}
