package io.neocore.common.player;

import com.treyzania.jzania.ExoContainer;

import io.neocore.api.player.IdentityProvider;
import io.neocore.api.player.NeoPlayer;
import io.neocore.api.player.PlayerIdentity;

public abstract class ProviderContainer {
	
	protected ExoContainer exo;
	
	private Class<? extends IdentityProvider<?>> serviceClass;
	
	public ProviderContainer(Class<? extends IdentityProvider<?>> sc) {
		this.serviceClass = sc;
	}
	
	public Class<? extends IdentityProvider<?>> getServiceClass() {
		return this.serviceClass;
	}
	
	public Class<? extends PlayerIdentity> getProvisionedClass() {
		return this.getProvider().getIdentityClass();
	}
	
	public abstract IdentityProvider<?> getProvider();
	
	public abstract ProvisionResult provide(NeoPlayer player);
	
	public abstract void unload(NeoPlayer player);
	
}
