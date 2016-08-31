package io.neocore.api.database;

import io.neocore.api.ServiceProvider;
import io.neocore.api.ServiceType;
import io.neocore.api.database.ban.BanService;
import io.neocore.api.database.group.GroupService;
import io.neocore.api.database.player.PlayerService;

/**
 * Enumerates which kinds of services that database controllers can provide.
 * 
 * @author treyzania
 */
public enum DatabaseService implements ServiceType {

	BAN(BanService.class), // Exactly what you think it is.
	LOGIN(null), // Player UUID, username, connecting address.
	SESSION(null), // Everything the SIMPLE_SESSION has, but also includes transactions as they move between proxied servers.
	PLAYER(PlayerService.class), // Core player data, extensions, etc.
	GROUP(GroupService.class), // Group definitions, flair, inheritance, tracks, etc.
	ARTIFACT(null), // Warnings, evidence, etc.
	ACTION_RECORD(null),
	
	MISC(null); // Miscellaneous sets of data with schema defined by modules.
	
	private Class<? extends DatabaseServiceProvider> serviceClass;
	
	private DatabaseService(Class<? extends DatabaseServiceProvider> clazz) {
		this.serviceClass = clazz;
	}
	
	public Class<? extends DatabaseServiceProvider> getProvider() {
		return this.serviceClass;
	}
	
	public boolean isCompatible(Class<? extends DatabaseServiceProvider> serv) {
		
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
