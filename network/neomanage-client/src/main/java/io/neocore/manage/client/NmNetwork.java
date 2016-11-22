package io.neocore.manage.client;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import io.neocore.manage.proto.NeomanageProtocol;

public class NmNetwork {
	
	public List<NmServer> servers;
	
	public NmNetwork(List<NmServer> servs) {
		this.servers = servs;
	}
	
	public NmServer getActiveServer() {
		
		for (NmServer serv : this.servers) {
			if (serv.isOnline()) return serv;
		}
		
		throw new IllegalStateException("No Neomanage servers active!");
		
	}
	
	public void broadcastLockChange(UUID uuid, NeomanageProtocol.SetLockState.LockType lockType, boolean state) {
		// TODO
	}
	
	public void broadcastUpdateSubscriptionState(UUID uuid, boolean state) {
		// TODO
	}
	
	public void broadcastUpdatePlayerList(Set<UUID> uuids) {
		// TODO
	}
	
}
