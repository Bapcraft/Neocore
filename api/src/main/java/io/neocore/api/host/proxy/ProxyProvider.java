package io.neocore.api.host.proxy;

import io.neocore.api.host.HostServiceProvider;
import io.neocore.api.player.NeoPlayer;

public interface ProxyProvider extends HostServiceProvider, NetworkParticipant {
	
	public void setProxyAcceptor(ProxyAcceptor acceptor);
	public ProxyAcceptor getProxyAcceptor();
	
	/**
	 * Moves the player from their current downstream server to another server. 
	 * 
	 * @param player The player
	 * @param server The server
	 */
	public void move(NeoPlayer player, NetworkEndpoint server);
	
}
