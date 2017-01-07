package io.neocore.common.net;

import io.neocore.common.player.CommonPlayerManager;

public class NetworkManagerImpl {
	
	private CommonPlayerManager assembler;
	
	private NetworkSync sync;
	
	public NetworkManagerImpl(CommonPlayerManager playerManager) {
		this.assembler = playerManager;
	}
	
	public void setNetworkSync(NetworkSync ns) {
		
		this.sync = ns;
		this.assembler.overrideNetworkSync(this.sync);
		
	}
	
	public NetworkSync getNetworkSync() {
		return this.sync;
	}
	
}
