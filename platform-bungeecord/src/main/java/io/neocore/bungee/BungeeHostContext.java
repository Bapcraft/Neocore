package io.neocore.bungee;

import io.neocore.api.host.HostContext;

public class BungeeHostContext extends HostContext {

	public BungeeHostContext(String name) {
		super(name);
	}

	@Override
	public String getFullName() {
		return this.getName();
	}

}
