package io.neocore.common.player;

/**
 * Represents how player data should be cached in the system.
 * 
 * @author treyzania
 */
public enum LoadType {

	/** Player is online on the server. */
	FULL,

	/** Player has been preloaded and is probably joining soon. */
	PRELOAD;

}
