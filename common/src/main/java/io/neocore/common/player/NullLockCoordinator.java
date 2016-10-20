package io.neocore.common.player;

import java.util.UUID;

import io.neocore.api.database.PersistentPlayerIdentity;

public class NullLockCoordinator<T extends PersistentPlayerIdentity> extends LockCoordinator<T> {
	
	@Override
	public int lock(UUID uuid) {
		return -1;
	}
	
	@Override
	public void unlock(UUID uuid) {
		
	}
	
	@Override
	public boolean isLocked(UUID uuid) {
		return false;
	}
	
	@Override
	public void blockUntilUnlocked(UUID uuid, long timeout) {
		
	}
	
}
