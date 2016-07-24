package io.neocore.api.database;

import io.neocore.api.ServiceProvider;
import io.neocore.api.ServiceType;
import io.neocore.api.database.group.GroupProvider;
import io.neocore.api.database.player.PlayerProvider;

/**
 * Enumerates which kinds of services that database controllers can provide.
 * 
 * @author treyzania
 */
public enum DatabaseService implements ServiceType {

	BAN(null), // Exactly what you think it is.
	LOGIN(null), // Player UUID, username, connecting address.
	SESSION(null), // Everything the SIMPLE_SESSION has, but also includes transactions as they move between proxied servers.
	PLAYER(PlayerProvider.class), // Core player data, extensions, etc.
	GROUP(GroupProvider.class), // Group definitions, flair, inheritance, tracks, etc.
	ARTIFACT(null), // Warnings, evidence, etc.
	ACTION_RECORD(null),
	ECO_TRANSACTION_RECORD(DatabaseServiceProvider.class),
	
	MISC(null); // Miscellaneous sets of data with schema defined by modules.
	
	private Class<? extends DatabaseServiceProvider> provider;
	
	private DatabaseService(Class<? extends DatabaseServiceProvider> clazz) {
		this.provider = clazz;
	}
	
	public Class<? extends DatabaseServiceProvider> getProvider() {
		return this.provider;
	}
	
	public boolean isCompatible(Class<? extends DatabaseServiceProvider> prov) {
		
		if (prov == null) return false;
		if (this.getProvider() == null) return false;
		
		return this.getProvider().isAssignableFrom(prov.getClass());
		
	}

	@Override
	public String getName() {
		return this.name();
	}

	@Override
	public Class<? extends ServiceProvider> getClassType() {
		return this.provider;
	}
	
}