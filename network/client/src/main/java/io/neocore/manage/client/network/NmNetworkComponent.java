package io.neocore.manage.client.network;

import java.util.UUID;

import io.neocore.api.AgentIdentity;

public interface NmNetworkComponent extends AgentIdentity {

	public void addPlayer(NmNetworkPlayer player);

	public boolean removePlayer(NmNetworkPlayer player);

	public boolean hasPlayerId(UUID uuid);

}
