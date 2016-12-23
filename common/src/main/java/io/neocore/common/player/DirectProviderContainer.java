package io.neocore.common.player;

import io.neocore.api.database.Persistent;
import io.neocore.api.player.IdentityProvider;
import io.neocore.api.player.NeoPlayer;
import io.neocore.api.player.PlayerIdentity;

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
			
			PlayerIdentity ident = this.getProvider().load(player.getUniqueId());
			
			if (ident != null) {
				
				// Call outward to the container.
				if (ident instanceof Persistent) {
					((Persistent) ident).setFlushProcedure(() -> player.flush());
				}
				
				player.addIdentity(ident);
				
			}
			
		});
		
		if (callback != null) callback.run();
		
		return ProvisionResult.IMMEDIATELY_INJECTED;
		
	}

	@Override
	public void flush(NeoPlayer player, Runnable callback) {
		
		if (this.isLinkage()) {
			
			this.exo.invoke("Flush(" + this.getProvider().getClass().getSimpleName() + ")-Direct", () -> {
				this.getProviderAsLinkage().flush(player.getUniqueId()); // Very simple here.
			});
			
		}
		
		if (callback != null) callback.run();
		
	}
	
	@Override
	public void unload(NeoPlayer player, Runnable callback) {
		
		this.exo.invoke("Unload(" + this.getProvider().getClass().getSimpleName() + ")-Direct", () -> {
			
			// Very simple here.
			this.getProvider().unload(player.getUniqueId());
			
		});
		
		if (callback != null) callback.run();
		
	}
	
}
