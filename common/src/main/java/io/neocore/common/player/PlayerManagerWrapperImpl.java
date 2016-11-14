package io.neocore.common.player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

import io.neocore.api.NeocoreAPI;
import io.neocore.api.ServiceType;
import io.neocore.api.player.NeoPlayer;
import io.neocore.api.player.PlayerManager;

public class PlayerManagerWrapperImpl implements PlayerManager {
	
	private CommonPlayerManager playerManager;
	
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
	public void preload(UUID uuid, Consumer<NeoPlayer> callback) {
		this.playerManager.assemblePlayer(uuid, LoadType.PRELOAD, callback);
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
	
}
