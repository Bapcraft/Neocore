package io.neocore.api.host;

import io.neocore.api.ServiceProvider;
import io.neocore.api.ServiceType;
import io.neocore.api.host.broadcast.BroadcastService;
import io.neocore.api.host.chat.ChatService;
import io.neocore.api.host.login.LoginService;
import io.neocore.api.host.permissions.PermissionsService;

/**
 * Enumerates which kinds of services that host plugins can provide.
 * 
 * @author treyzania
 */
public enum HostService implements ServiceType {

	LOGIN(LoginService.class), // When players connect, present on both
								// endpoints and proxies.
	BROADCAST(BroadcastService.class), // General broadcasts, regardless of
										// scale.
	PERMISSIONS(PermissionsService.class), // The ability to attach permissions
											// onto players for everyone to use.
	CHAT(ChatService.class), // General communication bound to some player-like
								// thing.
	GAMEPLAY(null); // Player teleportation, chest UI, scoreboards, etc.

	private Class<? extends HostServiceProvider> serviceClass;

	private HostService(Class<? extends HostServiceProvider> clazz) {
		this.serviceClass = clazz;
	}

	public boolean isCompatible(Class<? extends HostServiceProvider> serv) {

		if (serv == null)
			return false;
		if (this.getServiceClass() == null)
			return false;

		return this.getServiceClass().isAssignableFrom(serv.getClass());

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
