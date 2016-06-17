package io.neocore.host;

import io.neocore.host.broadcast.BroadcastProvider;
import io.neocore.host.chat.ChatProvider;
import io.neocore.host.proxy.ProxyProvider;

/**
 * Enumerates which kinds of services that host plugins can provide.
 * 
 * @author treyzania
 */
public enum HostService {
	
	LOGIN(null), // When players connect.
	PROXY(ProxyProvider.class), // Provides information as players connect and move around, as well as server statuses.  Generally just BungeeCord.
	BROADCAST(BroadcastProvider.class), // General broadcasts, regardless of scale.
	PERMISSIONS(null), // The ability to attach permissions onto players for everyone to use.
	CHAT(ChatProvider.class), // General communication bound to some player-like thing.
	GAMEPLAY(null); // Player teleportation, chest UI, scoreboards, etc.
	
	private Class<? extends HostServiceProvider> provider;
	
	private HostService(Class<? extends HostServiceProvider> clazz) {
		this.provider = clazz;
	}
	
	public Class<? extends HostServiceProvider> getProvider() {
		return this.provider;
	}
	
	public boolean isCompatible(Class<? extends HostServiceProvider> prov) {
		
		if (prov == null) return false;
		if (this.getProvider() == null) return false;
		
		return this.getProvider().isAssignableFrom(prov.getClass());
		
	}
	
}
