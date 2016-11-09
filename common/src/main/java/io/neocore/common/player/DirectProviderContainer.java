package io.neocore.common.player;

import io.neocore.api.player.IdentityProvider;
import io.neocore.api.player.NeoPlayer;

public class DirectProviderContainer extends ProviderContainer {
	
	private IdentityProvider<?> provider;
	
	public DirectProviderContainer(IdentityProvider<?> prov) {
		this.provider = prov;
	}
	
	@Override
	public IdentityProvider<?> getProvider() {
		return this.provider;
	}
	
	@Override
	public ProvisionResult load(NeoPlayer player, Runnable callback) {
		
		this.exo.invoke("Provide(" + this.getProvider().getClass().getSimpleName() + ")-Direct", () -> {
			
			// Very simple, we just have to drop it directly into the player.
			player.addIdentity(this.getProvider().load(player.getUniqueId()));
			if (callback != null) callback.run();
			
		});
		
		return ProvisionResult.IMMEDIATELY_INJECTED;
		
	}

	@Override
	public void flush(NeoPlayer player, Runnable callback) {
		
		if (this.isLinkage()) {
			
			this.exo.invoke("Flush(" + this.getProvider().getClass().getSimpleName() + ")-Direct", () -> {
				
				// Very simple here.
				this.getProviderAsLinkage().flush(player.getUniqueId());
				if (callback != null) callback.run();
				
			});
			
		}
		
	}
	
	@Override
	public void unload(NeoPlayer player, Runnable callback) {
		
		this.exo.invoke("Unload(" + this.getProvider().getClass().getSimpleName() + ")-Direct", () -> {
			
			// Very simple here.
			this.getProvider().unload(player.getUniqueId());
			if (callback != null) callback.run();
			
		});
		
	}
	
}
