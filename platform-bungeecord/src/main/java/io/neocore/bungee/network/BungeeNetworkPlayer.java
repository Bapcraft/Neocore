package io.neocore.bungee.network;

import java.net.InetAddress;
import java.util.UUID;

import io.neocore.api.infrastructure.NetworkEndpoint;
import io.neocore.api.infrastructure.NetworkPlayer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class BungeeNetworkPlayer implements NetworkPlayer {

	private DownstreamWrapper downstream;
	private ProxiedPlayer player;

	public BungeeNetworkPlayer(DownstreamWrapper down, ProxiedPlayer player) {

		this.downstream = down;
		this.player = player;

	}

	@Override
	public InetAddress getAddress() {
		return this.player.getAddress().getAddress();
	}

	@Override
	public void kick(String message) {
		this.player.disconnect(new TextComponent(message));
	}

	@Override
	public UUID getUniqueId() {
		return this.player.getUniqueId();
	}

	@Override
	public NetworkEndpoint getDownstreamServer() {
		return this.downstream.getEndpoint(player.getServer().getInfo().getName());
	}

	@Override
	public boolean isOnline() {
		return this.player.isConnected();
	}

}
