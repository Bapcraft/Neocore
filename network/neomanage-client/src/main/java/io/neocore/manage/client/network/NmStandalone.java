package io.neocore.manage.client.network;

import java.util.Set;
import java.util.UUID;

import io.neocore.api.infrastructure.NetworkPlayer;

public class NmStandalone extends RemoteAgent implements NmFrontend, NmEndpoint {
	
	private Set<NetworkPlayer> players;
	
	private NmStandalone(UUID id, String name) {
		super(id, name);
	}
	
	@Override
	public Set<NetworkPlayer> getPlayers() {
		return this.players;
	}
	
	public NetworkPlayer findPlayer(UUID uuid) {
		
		for (NetworkPlayer np : this.players) {
			if (np.getUniqueId().equals(uuid)) return np;
		}
		
		return null;
		
	}
	
	public void addPlayer(NetworkPlayer player) {
		this.players.add(player);
	}
	
	public void removePlayer(UUID uuid) {
		this.players.removeIf(p -> p.getUniqueId().equals(uuid));
	}
	
	public void removePlayer(NetworkPlayer np) {
		this.players.remove(np);
	}
	
}
