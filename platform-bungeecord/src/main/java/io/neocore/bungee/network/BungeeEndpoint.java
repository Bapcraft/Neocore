package io.neocore.bungee.network;

import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.Random;
import java.util.Set;

import io.neocore.api.host.proxy.NetworkEndpoint;
import io.neocore.api.host.proxy.NetworkPlayer;
import net.md_5.bungee.api.config.ServerInfo;

public class BungeeEndpoint extends NetworkEndpoint {
	
	private ServerInfo info;
	
	public BungeeEndpoint(ServerInfo si) {
		this.info = si;
	}
	
	@Override
	public String getName() {
		return this.info.getName();
	}
	
	@Override
	public InetSocketAddress getAddress() {
		return this.info.getAddress();
	}
	
	@Override
	public Set<NetworkPlayer> getPlayers() {
		return Collections.emptySet(); // TODO
	}
	
	
	@Override
	public int getPlayerCount() {
		return this.info.getPlayers().size();
	}

	@Override
	public void sendPlayer(NetworkPlayer player) {
		// TODO
	}

	@Override
	public int hashCode() {
		return (new Random(this.info.hashCode())).nextInt(); // Easy way to make it totally different but entirely deterministic.
	}
	
}
