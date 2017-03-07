package io.neocore.common.player;

import java.io.IOException;
import java.util.function.Consumer;

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
	public ProvisionResult load(NeoPlayer player, Consumer<LoadResult> callback) {

		PlayerIdentity ident = null;
		try {
			ident = this.getProvider().load(player.getUniqueId());
		} catch (IOException e) {

			if (callback != null)
				callback.accept(new LoadResult(ActionStatus.FAILURE, e));
			return ProvisionResult.INJECTION_NOT_POSSIBLE;

		} catch (RuntimeException e) {

			if (callback != null)
				callback.accept(new LoadResult(ActionStatus.FAILURE, e));
			return ProvisionResult.INJECTION_NOT_POSSIBLE;

		}

		if (ident != null) {

			// Call outward to the container.
			if (ident instanceof Persistent) {
				((Persistent) ident).setFlushProcedure(() -> player.flush());
			}

			player.addIdentity(ident);

		}

		if (callback != null)
			callback.accept(new LoadResult(ident != null ? ActionStatus.SUCCESS : ActionStatus.ABORTED, ident));
		return ident != null ? ProvisionResult.IMMEDIATELY_INJECTED : ProvisionResult.INJECTION_NOT_POSSIBLE;

	}

	@Override
	public void flush(NeoPlayer player, Consumer<FlushResult> callback) {

		boolean ok = true;
		if (this.isLinkage()) {

			try {
				this.getProviderAsLinkage().flush(player.getUniqueId());
			} catch (IOException e) {
				ok = false;
			} catch (RuntimeException e) {
				ok = false;
			}

		}

		if (callback != null)
			callback.accept(new FlushResult(ok ? ActionStatus.SUCCESS : ActionStatus.FAILURE));

	}

	@Override
	public void unload(NeoPlayer player, Consumer<UnloadResult> callback) {

		try {

			// Very simple here.
			this.getProvider().unload(player.getUniqueId());
			if (callback != null)
				callback.accept(new UnloadResult(ActionStatus.SUCCESS));

		} catch (RuntimeException e) {
			if (callback != null)
				callback.accept(new UnloadResult(ActionStatus.FAILURE));
		}

	}

}
