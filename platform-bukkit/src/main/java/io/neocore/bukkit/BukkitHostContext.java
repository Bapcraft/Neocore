package io.neocore.bukkit;

import io.neocore.api.host.HostContext;

public class BukkitHostContext extends HostContext {
	
	private String proxyName;
	
	public BukkitHostContext(String proxyName, String name) {
		
		super(name);
		
		this.proxyName = proxyName;
		
	}

	@Override
	public String getFullName() {
		return this.proxyName;
	}

}
