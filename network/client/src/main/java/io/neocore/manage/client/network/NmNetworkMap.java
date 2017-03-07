package io.neocore.manage.client.network;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import io.neocore.api.NeocoreAPI;
import io.neocore.api.infrastructure.ConnectionFrontend;
import io.neocore.api.infrastructure.NetworkEndpoint;
import io.neocore.api.infrastructure.NetworkMap;

public class NmNetworkMap implements NetworkMap {

	private NmFrontend frontend;
	private Set<NmEndpoint> endpoints;
	private Set<NmNetworkPlayer> players = new HashSet<>();

	public NmNetworkMap(NmFrontend frontend, Set<NmEndpoint> endpoints) {

		this.frontend = frontend;
		this.endpoints = endpoints;

	}

	public NmNetworkMap(NmProxyFrontend frontend) {
		this(frontend, new HashSet<>());
	}

	public NmNetworkMap(NmStandalone singularMember) {
		this(singularMember, new HashSet<>(Arrays.asList(singularMember)));
	}

	public void setFrontend(NmFrontend front) {
		this.frontend = front;
	}

	@Override
	public ConnectionFrontend getFrontend() {
		return this.frontend;
	}

	public NmFrontend getFrontend_Nm() {
		return this.frontend;
	}

	public void addEndpoint(NmEndpoint ep) {
		this.endpoints.add(ep);
	}

	public void removeEndpoint(UUID id) {
		this.endpoints.removeIf(e -> e.getAgentId().equals(id));
	}

	@Override
	public Set<NetworkEndpoint> getEndpoints() {
		return Collections.unmodifiableSet(this.endpoints);
	}

	public Set<NmEndpoint> getEndpoints_Nm() {
		return this.endpoints;
	}

	public boolean isEmpty() {
		return this.frontend == null && this.endpoints.isEmpty();
	}

	public boolean isValid() {
		return this.frontend != null && !this.endpoints.isEmpty();
	}

	public boolean containsMember(UUID uuid) {

		if (this.frontend.getAgentId().equals(uuid))
			return true;

		for (NetworkEndpoint ne : this.endpoints) {
			if (ne.getAgentId().equals(uuid))
				return true;
		}

		return false;

	}

	public void removeMember(UUID uuid) {

		if (this.frontend.getAgentId().equals(uuid))
			this.frontend = null;
		this.endpoints.removeIf(e -> e.getAgentId().equals(uuid));

	}

	public String getNetworkName() {

		if (this.getFrontend() != null) {
			return this.getFrontend().getNetworkName();
		} else if (this.endpoints.size() > 0) {
			return this.endpoints.iterator().next().getNetworkName(); // XXX Not
																		// perfect,
																		// but
																		// okay.
		} else {

			NeocoreAPI.getLogger().warning("Tried to get the name of a network that has no members at all.");
			return "[undefined]";

		}

	}

	private NmNetworkPlayer findPlayer(UUID uuid) {

		for (NmNetworkPlayer nnp : this.players) {
			if (nnp.getUniqueId().equals(uuid))
				return nnp;
		}

		return null;

	}

	public int removePlayer(UUID uuid) {

		int removed = 0;

		if (this.getFrontend_Nm() != null && this.getFrontend_Nm() instanceof RemoteAgent) {

			RemoteAgent ra = (RemoteAgent) this.getFrontend_Nm();
			if (ra.hasPlayer(uuid)) {

				ra.removePlayer(uuid);
				removed++;

			}

		}

		for (NmEndpoint ne : this.getEndpoints_Nm()) {

			if (ne instanceof RemoteAgent) {

				RemoteAgent ra = (RemoteAgent) ne;
				if (ra.hasPlayer(uuid)) {

					ra.removePlayer(uuid);
					removed++;

				}

			}

		}

		this.players.removeIf(np -> np.getUniqueId().equals(uuid));
		this.purgeAbsentPlayers();

		return removed;

	}

	public NmNetworkPlayer addPlayerTo(UUID uuid, NmNetworkComponent component) {

		if (component instanceof NmEndpoint && !this.endpoints.contains(component))
			throw new IllegalArgumentException("Tried to add a player to an endpoint not in its network.");

		NmNetworkPlayer nnp = this.findPlayer(uuid);
		if (nnp == null) {

			nnp = new NmNetworkPlayer(uuid);
			this.getFrontend_Nm().addPlayer(nnp);
			this.players.add(nnp);

		}

		component.addPlayer(nnp);

		return nnp;

	}

	public void removePlayerFrom(UUID uuid, NmNetworkComponent component) {

		if (component instanceof NmEndpoint && !this.endpoints.contains(component))
			throw new IllegalArgumentException("Tried to add a player to an endpoint not in its network.");

		NmNetworkPlayer nnp = this.findPlayer(uuid);
		if (nnp == null)
			return;

		component.removePlayer(nnp);
		this.purgeAbsentPlayers();

	}

	private void purgeAbsentPlayers() {

		Iterator<NmNetworkPlayer> iter = this.players.iterator();
		while (iter.hasNext()) {

			NmNetworkPlayer nnp = iter.next();
			int count = 0;

			if (this.getFrontend_Nm().hasPlayer(nnp.getUniqueId()))
				count++;
			for (NmEndpoint ne : this.getEndpoints_Nm()) {
				if (ne.hasPlayerId(nnp.getUniqueId()))
					count++;
			}

			if (count == 0)
				iter.remove();

		}

	}

}
