package io.neocore.module;

public interface Micromodule extends Module {
	
	public default void onLoad() {
		
	}
	
	public void onEnable();
	public void onDisable();
	
}
