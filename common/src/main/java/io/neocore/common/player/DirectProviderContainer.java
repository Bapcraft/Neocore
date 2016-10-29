package io.neocore.common.player;

import io.neocore.api.player.IdentityProvider;
import io.neocore.api.player.NeoPlayer;

public class DirectProviderContainer extends ProviderContainer {
	
	private IdentityProvider<?> provider;
	
	public DirectProviderContainer(Class<? extends IdentityProvider<?>> sc, IdentityProvider<?> prov) {
		
		super(sc);
		
		this.provider = prov;
		
	}
	
	@Override
	public IdentityProvider<?> getProvider() {
		return this.provider;
	}
	
	@Override
	public ProvisionResult provide(NeoPlayer player) {
		
		this.exo.invoke("Provide(" + this.getProvider().getClass().getSimpleName() + ")-Direct", () -> {
			
			// Very simple, we just have to drop it directly into the player.
			player.addIdentity(this.getProvider().load(player.getUniqueId()));
			
		});
		
		return ProvisionResult.IMMEDIATELY_INJECTED;
		
	}

	@Override
	public void unload(NeoPlayer player) {
		
		this.exo.invoke("Unload(" + this.getProvider().getClass().getSimpleName() + ")-Direct", () -> {
			
			// Very simple here.
			this.provider.unload(player.getUniqueId());
			
		});
		
	}
	
}
