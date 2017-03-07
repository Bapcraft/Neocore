package io.neocore.common.player;

import java.io.IOException;
import java.util.function.Consumer;

import io.neocore.api.database.IdentityLinkage;
import io.neocore.api.player.IdentityProvider;
import io.neocore.api.player.NeoPlayer;
import io.neocore.api.player.PlayerIdentity;
import io.neocore.api.task.DumbTaskDelegator;
import io.neocore.api.task.TaskDelegator;
import io.neocore.api.task.TaskQueue;

public class DeferredProviderContainer extends ProviderContainer {

	private IdentityProvider<?> provider;
	private TaskQueue queue;

	private TaskDelegator delegator;

	public DeferredProviderContainer(IdentityProvider<?> prov, TaskQueue queue) {

		this.provider = prov;
		this.queue = queue;

		this.delegator = new DumbTaskDelegator("DeferredProvider:" + prov.getIdentityClass().getSimpleName());

	}

	@Override
	public IdentityProvider<?> getProvider() {
		return this.provider;
	}

	@Override
	public ProvisionResult load(NeoPlayer player, Consumer<LoadResult> callback) {

		this.addWrappedRunnableToQueue("load " + player.getUniqueId(), () -> {

			PlayerIdentity pi = null;

			try {

				pi = this.provider.load(player.getUniqueId());
				if (pi != null) {

					player.addIdentity(pi);

				}

			} catch (IOException e) {

				if (callback != null)
					callback.accept(new LoadResult(ActionStatus.FAILURE, e));
				return;

			} catch (RuntimeException e) {

				if (callback != null)
					callback.accept(new LoadResult(ActionStatus.FAILURE, e));
				return;

			}

			if (callback != null)
				callback.accept(new LoadResult(pi != null ? ActionStatus.SUCCESS : ActionStatus.ABORTED, pi));

		});

		return ProvisionResult.QUEUED_DEFERRED_INJECTION;

	}

	@Override
	public void flush(NeoPlayer player, Consumer<FlushResult> callback) {

		if (!(provider instanceof IdentityLinkage)) {

			return;

		}

		IdentityLinkage<?> link = this.getProviderAsLinkage();

		this.addWrappedRunnableToQueue("flush " + player.getUniqueId(), () -> {

			try {
				if (player.hasIdentity(link.getIdentityClass()))
					link.flush(player.getUniqueId());
			} catch (Throwable t) {

				if (callback != null)
					callback.accept(new FlushResult(ActionStatus.FAILURE));
				return;

			}

			if (callback != null)
				callback.accept(new FlushResult(ActionStatus.SUCCESS));

		});

	}

	@Override
	public void unload(NeoPlayer player, Consumer<UnloadResult> callback) {

		this.addWrappedRunnableToQueue("unload " + player.getUniqueId(), () -> {

			try {
				this.provider.unload(player.getUniqueId());
			} catch (RuntimeException t) {

				if (callback != null)
					callback.accept(new UnloadResult(ActionStatus.FAILURE));
				return;

			}

			if (callback != null)
				callback.accept(new UnloadResult(ActionStatus.SUCCESS));

		});

	}

	private void addWrappedRunnableToQueue(String name, Runnable r) {
		this.queue.enqueue(new NamedRunnableTask(this.delegator, name, r));
	}

}
