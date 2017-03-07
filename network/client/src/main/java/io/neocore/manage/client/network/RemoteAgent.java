package io.neocore.manage.client.network;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import io.neocore.api.AgentIdentity;
import io.neocore.api.infrastructure.NetworkHost;
import io.neocore.api.infrastructure.NetworkPlayer;

public class RemoteAgent implements AgentIdentity, NetworkHost, NmNetworkComponent {

	private UUID agentId;
	private String name, network;

	private Set<NmNetworkPlayer> players = new HashSet<>();

	public RemoteAgent(UUID id, String name, String network) {

		this.agentId = id;
		this.name = name;
		this.network = network;

	}

	public RemoteAgent(UUID id, String name) {
		this(id, name, null);
	}

	@Override
	public UUID getAgentId() {
		return this.agentId;
	}

	@Override
	public String getAgentName() {
		return this.name;
	}

	@Override
	public String getNetworkName() {
		return this.network;
	}

	@Override
	public Set<NetworkPlayer> getPlayers() {
		return Collections.unmodifiableSet(this.players);
	}

	@Override
	public void addPlayer(NmNetworkPlayer nnp) {
		this.players.add(nnp);
	}

	public void removePlayer(UUID uuid) {
		this.players.removeIf(p -> p.getUniqueId().equals(uuid));
	}

	@Override
	public boolean removePlayer(NmNetworkPlayer nnp) {
		return this.players.remove(nnp);
	}

	@Override
	public boolean hasPlayerId(UUID uuid) {

		for (NmNetworkPlayer nnp : this.players) {
			if (nnp.getUniqueId().equals(uuid))
				return true;
		}

		return false;

	}

}
