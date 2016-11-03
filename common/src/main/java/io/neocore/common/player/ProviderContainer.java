package io.neocore.common.player;

import com.treyzania.jzania.ExoContainer;

import io.neocore.api.NeocoreAPI;
import io.neocore.api.player.IdentityProvider;
import io.neocore.api.player.NeoPlayer;
import io.neocore.api.player.PlayerIdentity;

public abstract class ProviderContainer {
	
	protected ExoContainer exo;
	
	public ProviderContainer() {
		
		this.exo = new ExoContainer(NeocoreAPI.getLogger());
		
		this.exo.addHook(ct -> {
			NeocoreAPI.getLogger().severe("Problem initializing player identity: " + ct.getThrown().getMessage());
		});
		
	}
	
	public Class<? extends PlayerIdentity> getProvisionedClass() {
		return this.getProvider().getIdentityClass();
	}
	
	public abstract IdentityProvider<?> getProvider();
	
	public abstract ProvisionResult provide(NeoPlayer player, Runnable callback);
	
	public abstract void flush(NeoPlayer player, Runnable callback);
	
	public abstract void unload(NeoPlayer player, Runnable callback);
	
}
