package io.neocore.common.player;

import io.neocore.common.net.LockCoordinator;

public interface LockableContainer {

	public void overrideLockCoordinator(LockCoordinator override);

}
