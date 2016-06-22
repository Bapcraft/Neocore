package io.neocore.host.proxy;

import java.util.UUID;

import io.neocore.host.HostServiceProvider;
import io.neocore.player.NeoPlayer;

public interface ProxyProvider extends HostServiceProvider {
	
	public void setProxyAcceptor(ProxyAcceptor acceptor);
	public ProxyAcceptor getProxyAcceptor();
	
	/**
	 * Moves the player from their current downstream server to another server. 
	 * 
	 * @param player The player
	 * @param server The server
	 */
	public void move(NeoPlayer player, DownstreamServer server);
	
	/**
	 * @return An array of all downstream servers this host can connect to.
	 */
	public DownstreamServer[] getDownstreamServers();
	
	/**
	 * Gets the proxied player information for the specified player.
	 * 
	 * @param uuid The UUID of the player
	 * @return The proxy state information
	 */
	public ProxiedPlayer getPlayer(UUID uuid);
	
}
