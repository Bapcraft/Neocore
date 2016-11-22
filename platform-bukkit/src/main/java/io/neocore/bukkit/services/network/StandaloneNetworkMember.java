package io.neocore.bukkit.services.network;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import io.neocore.api.infrastructure.ConnectionFrontend;
import io.neocore.api.infrastructure.NetworkEndpoint;
import io.neocore.api.infrastructure.NetworkPlayer;

public class StandaloneNetworkMember implements ConnectionFrontend, NetworkEndpoint {
	
	private String name;
	
	public StandaloneNetworkMember(String fakeNetName) {
		this.name = fakeNetName;
	}
	
	@Override
	public String getNetworkName() {
		return this.name;
	}
	
	@Override
	public String getEndpointName() {
		return this.name;
	}
	
	@Override
	public Set<NetworkPlayer> getPlayers() {
		
		Set<NetworkPlayer> players = new HashSet<>();
		
		for (Player p : Bukkit.getOnlinePlayers()) {
			players.add(new StandaloneNetPlayer(p, this));
		}
		
		return players;
		
	}
	
}
