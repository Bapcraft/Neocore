package io.neocore.bungee.network;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import io.neocore.api.host.proxy.NetworkEndpoint;
import io.neocore.api.host.proxy.NetworkPlayer;
import io.neocore.api.host.proxy.ProxyAcceptor;
import io.neocore.api.host.proxy.ProxyProvider;
import io.neocore.bungee.events.ProxyForwarder;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

public class BungeeProxyService implements ProxyProvider {
	
	private ProxyServer bungee;
	
	private ProxyForwarder forwarder;
	
	public BungeeProxyService(ProxyServer bungee) {
		this.bungee = bungee;
	}
	
	@Override
	public Set<NetworkEndpoint> getNetworkEndpoints() {
		
		// Just wrap all of the infos directly.
		Set<NetworkEndpoint> endpoints = new HashSet<>();
		this.bungee.getServers().values().forEach(si -> endpoints.add(new BungeeEndpoint(si)));
		
		return endpoints;
		
	}
	
	@Override
	public void move(NetworkPlayer player, NetworkEndpoint server) {
		
		ServerInfo si = this.bungee.getServerInfo(server.getName());
		this.bungee.getPlayer(player.getUniqueId()).connect(si);
		
	}
	
	@Override
	public NetworkPlayer getPlayer(UUID uuid) {
		return null; // TODO Caching system to avoid memory leaks and shit.
	}
	
	@Override
	public void setProxyAcceptor(ProxyAcceptor acceptor) {
		this.forwarder.dest = acceptor;
	}
	
	@Override
	public ProxyAcceptor getProxyAcceptor() {
		return this.forwarder.dest;
	}
	
}
