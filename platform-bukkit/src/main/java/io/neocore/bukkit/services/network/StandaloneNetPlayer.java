package io.neocore.bukkit.services.network;

import java.net.InetAddress;
import java.util.UUID;

import org.bukkit.entity.Player;

import io.neocore.api.infrastructure.NetworkEndpoint;
import io.neocore.api.infrastructure.NetworkPlayer;

public class StandaloneNetPlayer implements NetworkPlayer {
	
	private Player player;
	private NetworkEndpoint endpoint;
	
	public StandaloneNetPlayer(Player p, NetworkEndpoint self) {
		
		this.player = p;
		this.endpoint = self;
		
	}
	
	@Override
	public UUID getUniqueId() {
		return this.player.getUniqueId();
	}
	
	@Override
	public InetAddress getAddress() {
		return this.player.getAddress().getAddress();
	}

	@Override
	public void kick(String message) {
		this.player.kickPlayer(message);
	}
	
	@Override
	public NetworkEndpoint getDownstreamServer() {
		return this.endpoint;
	}

	@Override
	public int hashCode() {
		return this.player.hashCode();
	}
	
}
