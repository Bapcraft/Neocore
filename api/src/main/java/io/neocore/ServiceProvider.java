package io.neocore;

public interface ServiceProvider {
	
	/**
	 * Called when the services are being initialized.
	 */
	public default void init() {
		
	}
	
	/**
	 * Called when services should shutdown connections to downstream systems and do cleanup.
	 */
	public default void finish() {
		
	}
	
}
