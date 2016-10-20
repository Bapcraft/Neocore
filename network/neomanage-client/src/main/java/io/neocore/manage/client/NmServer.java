package io.neocore.manage.client;

import java.net.InetSocketAddress;
import java.util.UUID;

import io.neocore.manage.proto.NeomanageProtocol;

public class NmServer {
	
	private boolean isOnline;
	private InetSocketAddress address;
	
	public NmServer(InetSocketAddress addr) {
		this.address = addr;
	}
	
	public boolean isOnline() {
		return this.isOnline;
	}
	
	public InetSocketAddress getRemoteSocket() {
		return this.address;
	}
	
	public void broadcastLock(UUID uuid, NeomanageProtocol.SetLockState.LockType lockType) {
		// TODO
	}
	
	public void broadcastUnlock(UUID uuid, NeomanageProtocol.SetLockState.LockType lockType) {
		// TODO
	}
	
}
