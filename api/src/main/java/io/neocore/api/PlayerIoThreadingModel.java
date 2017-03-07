package io.neocore.api;

/**
 * Used to determine which threading model to load players with.
 * 
 * @author treyzania
 */
public enum PlayerIoThreadingModel {

	SINGLE_THREAD, FORCE_ASYNC, FORCE_DEFERRED,

	AUTO, AUTO_DEFERRED;

}
