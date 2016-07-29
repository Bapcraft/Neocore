package io.neocore.api;

public interface NeocoreConfig {
	
	/**
	 * @return The name of the server not only on the network, but in the database as well.
	 */
	public String getServerName();
	
	/**
	 * @return If we should check for bans and kick people as applicable.
	 */
	public boolean isEnforcingBans();
	
	/**
	 * @return If this servers is merely an endpoint in a larger network, not where players directly connect.
	 */
	public boolean isNetworked();
	
}
