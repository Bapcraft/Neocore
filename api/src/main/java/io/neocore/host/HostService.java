package io.neocore.host;

/**
 * Enumerates which kinds of services that host plugins can provide.
 * 
 * @author treyzania
 */
public enum HostService {
	
	LOGIN, // When players connect.
	PROXY, // Provides information as players connect and move around, as well as server statuses.
	BROADCAST, // General broadcasts, regardless of scale.
	PERMISSIONS, // The ability to attach permissions onto players for everyone to use.
	CHAT, // General communication bound to some player-like thing.
	GAMEPLAY; // Player teleportation, chest UI, scoreboards, etc.
	
}
