package io.neocore.database;

/**
 * Enumerates which kinds of services that database controllers can provide.
 * 
 * @author treyzania
 */
public enum DatabaseService {

	BAN, // Exactly what you think it is.
	SIMPLE_SESSION, // Player UUID, username, connecting address.
	ADVANCED_SESSION, // Everything the SIMPLE_SESSION has, but also includes transactions as they move between proxied servers.
	PLAYER, // Core player data, extensions, etc.
	GROUPS, // Group definitions, flair, inheritance, tracks, etc.
	ARTIFACT, // Warnings, evidence, etc.
	CHAT_LOG,
	
	MISC; // Miscellaneous sets of data with schema defined by modules.
	
}
