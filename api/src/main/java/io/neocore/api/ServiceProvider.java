package io.neocore.api;

public interface ServiceProvider {

	/**
	 * Called when the services are being initialized.
	 */
	public default void init() {

	}

	/**
	 * Called whever services should look at their underlying data and update
	 * any caches. Preferably this would be done in a way that dependent
	 * downstream systems would either acknowledge the "dirtiness" of their
	 * information or would seamlessly update the cached valiues.
	 */
	public default void reload() {

	}

	/**
	 * Called when services should shutdown connections to downstream systems
	 * and do cleanup.
	 */
	public default void finish() {

	}

}
