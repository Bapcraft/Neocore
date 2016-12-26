package io.neocore.api;

import java.util.List;

import io.neocore.api.host.Context;

public interface NeocoreConfig {
	
	/**
	 * @return The name of the server not only on the network, but in the database as well
	 */
	public String getServerName();
	
	/**
	 * @return The name of the network the server is on, <code>null</code> if standalone
	 */
	public String getNetworkName();
	
	/**
	 * @return If we should check for bans and kick people as applicable
	 */
	public boolean isEnforcingBans();
	
	/**
	 * @return If this servers is merely an endpoint in a larger network, not where players directly connect
	 */
	public boolean isNetworked();
	
	/**
	 * @return The "primary" context of the host
	 */
	public Context getPrimaryContext();
	
	/**
	 * @return A list of all host contexts, including the primary one
	 */
	public List<Context> getContexts();
	
	/**
	 * @return Which threading model to use for player load/flush/etc
	 */
	public PlayerIoThreadingModel getPlayerThreadingModel();
	
}
