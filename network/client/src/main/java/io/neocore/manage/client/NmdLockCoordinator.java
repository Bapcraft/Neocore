package io.neocore.manage.client;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import io.neocore.common.net.LockCoordinator;
import io.neocore.manage.client.net.NmNetwork;
import io.neocore.manage.proto.ClientMessageUtils;
import io.neocore.manage.proto.NeomanageProtocol.ClientMessage;
import io.neocore.manage.proto.NeomanageProtocol.SetLockState;

public class NmdLockCoordinator extends LockCoordinator {
	
	private UUID agentId;
	
	private NmNetwork network;
	private LockManager locker;
	
	public NmdLockCoordinator(UUID agentId, NmNetwork net) {
		
		this.agentId = agentId;
		
		this.network = net;
		this.locker = new LockManager();
		
	}
	
	public LockManager getLockManager() {
		return this.locker;
	}
	
	@Override
	public int lock(UUID uuid) {
		
		this.sendLockUpdate(uuid, true);
		this.locker.lock(uuid);
		
		return 0;
		
	}
	
	@Override
	public void unlock(UUID uuid) {
		
		this.sendLockUpdate(uuid, false);
		this.locker.release(uuid);
		
	}
	
	private void sendLockUpdate(UUID uuid, boolean state) {

		ClientMessage.Builder b = ClientMessageUtils.newBuilder(this.agentId);
		SetLockState.Builder slsb = SetLockState.newBuilder();
		
		slsb.setUuid(uuid.toString());
		slsb.setState(state);
		b.setSetLockState(slsb);
		
		this.network.getActiveServer().queueMessage(b.build());
		
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
