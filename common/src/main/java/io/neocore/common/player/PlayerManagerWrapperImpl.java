package io.neocore.common.player;

import java.util.UUID;
import java.util.function.Consumer;

import com.treyzania.jzania.ExoContainer;

import io.neocore.api.NeocoreAPI;
import io.neocore.api.event.EventManager;
import io.neocore.api.event.database.LoadReason;
import io.neocore.api.event.database.PostLoadPlayerEvent;
import io.neocore.api.player.NeoPlayer;
import io.neocore.api.player.PlayerManager;

public class PlayerManagerWrapperImpl implements PlayerManager {
	
	private ExoContainer container;
	private CommonPlayerManager playerManager;
	private EventManager eventManager;
	
	public PlayerManagerWrapperImpl(CommonPlayerManager man, EventManager events) {
		
		this.playerManager = man;
		this.eventManager = events;
		
		this.container = new ExoContainer(NeocoreAPI.getLogger());
		
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
	public NeoPlayer startInit(UUID uuid, Consumer<NeoPlayer> callback) {
		
		return this.playerManager.assemblePlayer(uuid, np -> {
			
			np.setPopulated();
			if (callback != null) this.container.invoke("InitPlayerCallback(" + uuid + ")", () -> callback.accept(np));
			
			this.eventManager.broadcast(new PostLoadPlayerEvent(LoadReason.JOIN, np));
			
		});
		
	}
	
	@Override
	public NeoPlayer getPlayer(UUID uuid) {
		return null;
	}
	
}
