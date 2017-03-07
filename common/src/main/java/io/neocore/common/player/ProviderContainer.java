package io.neocore.common.player;

import java.util.function.Consumer;

import com.treyzania.jzania.ExoContainer;

import io.neocore.api.NeocoreAPI;
import io.neocore.api.database.IdentityLinkage;
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

	public boolean isLinkage() {
		return this.getProvider() instanceof IdentityLinkage;
	}
	
	public IdentityLinkage<?> getProviderAsLinkage() {
		return (IdentityLinkage<?>) this.getProvider();
	}
	
	public abstract ProvisionResult load(NeoPlayer player, Consumer<LoadResult> callback);
	
	public abstract void flush(NeoPlayer player, Consumer<FlushResult> callback);
	
	public abstract void unload(NeoPlayer player, Consumer<UnloadResult> callback);
	
}
