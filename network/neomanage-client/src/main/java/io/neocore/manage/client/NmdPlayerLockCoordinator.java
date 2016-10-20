package io.neocore.manage.client;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import io.neocore.api.database.player.DatabasePlayer;
import io.neocore.common.player.LockCoordinator;

public class NmdPlayerLockCoordinator extends LockCoordinator<DatabasePlayer> {
	
	private NmNetwork network;
	private LockManager<DatabasePlayer> locker;
	
	public NmdPlayerLockCoordinator(NmNetwork net) {
		
		this.network = net;
		this.locker = new LockManager<>();
		
	}
	
	public LockManager<DatabasePlayer> getLocker() {
		return this.locker;
	}
	
	@Override
	public int lock(UUID uuid) {
		
		this.network.getActiveServer().broadcastLock(uuid, LockType.PLAYER);
		this.locker.lock(uuid);
		
		return 0;
		
	}

	@Override
	public void unlock(UUID uuid) {
		
		this.network.getActiveServer().broadcastUnlock(uuid, LockType.PLAYER);
		this.locker.lock(uuid);
		
	}

	@Override
	public boolean isLocked(UUID uuid) {
		return this.locker.isLocked(uuid);
	}

	@Override
	public void blockUntilUnlocked(UUID uuid, long timeout) {
		
		CountDownLatch latch = new CountDownLatch(1);
		
		this.locker.queue(uuid, () -> {
			latch.countDown();
		});
		
		try {
			latch.await(timeout, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			// ehh?
		}
		
	}

}
