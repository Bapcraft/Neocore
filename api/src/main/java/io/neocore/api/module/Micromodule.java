package io.neocore.api.module;

public interface Micromodule extends Module {
	
	public static final String MICROMODULE_CONFIGURATION_FILE = "micromodule.conf";
	
	public default void onLoad() {
		
	}
	
	public void onEnable();
	public void onDisable();
	
}
