package io.neocore.bukkit.services.network;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import io.neocore.api.Neocore;
import io.neocore.api.infrastructure.ConnectionFrontend;
import io.neocore.api.infrastructure.NetworkEndpoint;
import io.neocore.api.infrastructure.NetworkPlayer;

public class StandaloneNetworkMember implements ConnectionFrontend, NetworkEndpoint {

	private Neocore neocore;

	public StandaloneNetworkMember(Neocore neo) {
		this.neocore = neo;
	}

	@Override
	public UUID getAgentId() {
		return this.neocore.getAgentId();
	}

	@Override
	public String getAgentName() {
		return this.neocore.getAgentName();
	}

	@Override
	public String getNetworkName() {
		return this.neocore.getNetworkName();
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
