package io.neocore.host.proxy;

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
	
}
