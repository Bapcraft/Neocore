package io.neocore.api.host;

import io.neocore.api.ServiceProvider;
import io.neocore.api.ServiceType;
import io.neocore.api.host.broadcast.BroadcastService;
import io.neocore.api.host.chat.ChatService;
import io.neocore.api.host.login.LoginService;
import io.neocore.api.host.permissions.PermissionsService;
import io.neocore.api.host.proxy.EndpointService;
import io.neocore.api.host.proxy.ProxyProvider;

/**
 * Enumerates which kinds of services that host plugins can provide.
 * 
 * @author treyzania
 */
public enum HostService implements ServiceType {
	
	LOGIN(LoginService.class), // When players connect, present on both endpoints and proxies.
	PROXY(ProxyProvider.class), // Provides information as players connect and move around, as well as server statuses.  Generally just BungeeCord.
	ENDPOINT(EndpointService.class), // If this server is an endpoint that proxied players connect to via something like BungeeCord.
	BROADCAST(BroadcastService.class), // General broadcasts, regardless of scale.
	PERMISSIONS(PermissionsService.class), // The ability to attach permissions onto players for everyone to use.
	CHAT(ChatService.class), // General communication bound to some player-like thing.
	GAMEPLAY(null); // Player teleportation, chest UI, scoreboards, etc.
	
	private Class<? extends HostServiceProvider> serviceClass;
	
	private HostService(Class<? extends HostServiceProvider> clazz) {
		this.serviceClass = clazz;
	}
	
	public Class<? extends HostServiceProvider> getProvider() {
		return this.serviceClass;
	}
	
	public boolean isCompatible(Class<? extends HostServiceProvider> serv) {
		
		if (serv == null) return false;
		if (this.getProvider() == null) return false;
		
		return this.getProvider().isAssignableFrom(serv.getClass());
		
	}

	@Override
	public String getName() {
		return this.name();
	}

	@Override
	public Class<? extends ServiceProvider> getServiceClass() {
		return this.serviceClass;
	}
	
}
