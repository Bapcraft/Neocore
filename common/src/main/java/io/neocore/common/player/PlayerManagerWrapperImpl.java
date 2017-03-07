package io.neocore.common.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

import io.neocore.api.NeocoreAPI;
import io.neocore.api.ServiceType;
import io.neocore.api.event.database.FlushReason;
import io.neocore.api.event.database.LoadReason;
import io.neocore.api.event.database.UnloadReason;
import io.neocore.api.player.NeoPlayer;
import io.neocore.api.player.PlayerLease;
import io.neocore.api.player.PlayerManager;

public class PlayerManagerWrapperImpl implements PlayerManager {

	private CommonPlayerManager playerManager;

	private Map<UUID, List<PlayerLeaseImpl>> leases = new HashMap<>();

	public PlayerManagerWrapperImpl(CommonPlayerManager man) {
		this.playerManager = man;
	}

	@Override
	public boolean isInited(UUID uuid) {
		return this.playerManager.isInited(uuid);
	}

	@Override
	public boolean isPopulated(UUID uuid) {
		return this.playerManager.isPopulated(uuid);
	}

	@Override
	public NeoPlayer getPlayer(UUID uuid) {
		return this.playerManager.findPlayer(uuid);
	}

	@Override
	public void addService(ServiceType type) {

		NeocoreAPI.getLogger().info("Registering identity provider type: " + type);
		this.playerManager.addService(type);

	}

	@Override
	public Set<NeoPlayer> getOnlinePlayers() {
		return new HashSet<>(this.playerManager.playerCache);
	}

	@Override
	public synchronized PlayerLease requestLease(UUID uuid) {
		return this.requestLease(uuid, null);
	}

	private void flagLeases(NeoPlayer player) {

		// We have to duplicate this list because otherwise we'd end up removing
		// things while we're iterating.
		List<PlayerLeaseImpl> plis = new ArrayList<>(this.leases.get(player.getUniqueId()));

		for (PlayerLeaseImpl pl : plis) {
			pl.flagCompleted();
		}

	}

	protected void releaseLease(PlayerLeaseImpl pli) {

		UUID uuid = pli.getUniqueId();
		List<PlayerLeaseImpl> plis = this.leases.get(uuid);
		NeocoreAPI.getLogger().fine("Releasing lease for " + uuid + ", " + (plis.size() - 1) + " remaining.");

		if (plis.remove(pli)) {

			if (plis.size() == 0) {

				this.leases.remove(uuid);

				NeoPlayer np = this.playerManager.findPlayer(uuid);

				this.playerManager.flushPlayer(uuid, FlushReason.OTHER, () -> {

					NeocoreAPI.getLogger().finest("Flush of " + uuid + " completed for unload, now unloading...");
					this.playerManager.unloadPlayer(uuid, UnloadReason.OTHER, () -> {
						NeocoreAPI.getLogger()
								.fine("Unloaded player " + uuid + " (hashcode: " + np.hashCode() + ") from Neocore.");
					});

				});

			}

		}

	}

	@Override
	public synchronized PlayerLease requestLease(UUID uuid, Consumer<NeoPlayer> callback) {

		List<PlayerLeaseImpl> playerLeases;

		if (!this.leases.containsKey(uuid)) {

			playerLeases = new ArrayList<>();
			this.leases.put(uuid, playerLeases);

		} else {
			playerLeases = this.leases.get(uuid);
		}

		PlayerLeaseImpl lease = new PlayerLeaseImpl(uuid, this, this.playerManager);
		if (callback != null)
			lease.addCallback(callback);

		playerLeases.add(lease);
		NeocoreAPI.getLogger()
				.fine("Lease for " + uuid + " requested, for a total of " + playerLeases.size() + " leases.");

		// Actually start construction.
		if (!this.isInited(uuid))
			this.playerManager.assemblePlayer(uuid, LoadReason.OTHER, this::flagLeases);

		// If it's okay then flag it already
		if (this.isPopulated(uuid))
			lease.flagCompleted();

		return lease;

	}

	public Map<UUID, List<PlayerLeaseImpl>> getLeases() {
		return this.leases;
	}

}
