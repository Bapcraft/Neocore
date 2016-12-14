package io.neocore.api.module;

import com.typesafe.config.Config;

import io.neocore.api.Neocore;

/**
 * A mini plugin that can operate on a wider range of server types/designs.
 * 
 * @author treyzania
 */
public interface Micromodule extends Module {
	
	/**
	 * The filename in the micromodule archive that the metadata is stored in
	 */
	public static final String MICROMODULE_CONFIGURATION_FILE = "micromodule.conf";
	
	/**
	 * Called immediately after the micromodule is instantiated
	 */
	public default void onLoad() {
		
	}
	
	/**
	 * Called before being enabled to set up the configuration of the micromodule.
	 * 
	 * @param config The HOCON configuration object
	 */
	public default void configure(Config config) {
		
	}
	
	/**
	 * Called when all of the other micromodules are being enabled, after they've all been loaded
	 */
	public void onEnable();
	
	/**
	 * Called when the micromodule is being disabled, such as when the server is being shut down
	 */
	public void onDisable();
	
	/**
	 * @return Gets the instance of Neocore that loaded the micromodule
	 */
	public Neocore getAgent();
	
}
