package io.neocore.bungee.network;

import java.net.InetAddress;
import java.util.UUID;

import io.neocore.api.host.proxy.NetworkEndpoint;
import io.neocore.api.host.proxy.NetworkPlayer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class BungeeNetworkPlayer implements NetworkPlayer {
	
	private ProxiedPlayer player;
	
	public BungeeNetworkPlayer(ProxiedPlayer player) {
		this.player = player;
	}
	
	@Override
	public InetAddress getAddress() {
		return this.player.getAddress().getAddress();
	}

	@Override
	public void kick(String message) {
		// TODO
	}

	@Override
	public UUID getUniqueId() {
		return this.player.getUniqueId();
	}

	@Override
	public NetworkEndpoint getDownstreamServer() {
		return new BungeeEndpoint(this.player.getServer().getInfo());
	}

}
