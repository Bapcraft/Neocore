package io.neocore.database;

import io.neocore.database.group.GroupProvider;
import io.neocore.database.player.PlayerProvider;

/**
 * Enumerates which kinds of services that database controllers can provide.
 * 
 * @author treyzania
 */
public enum DatabaseService {

	BAN(null), // Exactly what you think it is.
	LOGINS(null), // Player UUID, username, connecting address.
	SESSION(null), // Everything the SIMPLE_SESSION has, but also includes transactions as they move between proxied servers.
	PLAYER(PlayerProvider.class), // Core player data, extensions, etc.
	GROUPS(GroupProvider.class), // Group definitions, flair, inheritance, tracks, etc.
	ARTIFACT(null), // Warnings, evidence, etc.
	ACTION_LOG(null),
	
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
	
}
