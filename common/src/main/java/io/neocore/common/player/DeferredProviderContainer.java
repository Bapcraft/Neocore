package io.neocore.common.player;

import java.util.logging.Level;

import io.neocore.api.NeocoreAPI;
import io.neocore.api.database.IdentityLinkage;
import io.neocore.api.player.IdentityProvider;
import io.neocore.api.player.NeoPlayer;
import io.neocore.api.player.PlayerIdentity;
import io.neocore.api.task.DumbTaskDelegator;
import io.neocore.api.task.RunnableTask;
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
	public ProvisionResult load(NeoPlayer player, Runnable callback) {
		
		this.addWrappedRunnableToQueue(() -> {
			
			try {
				
				PlayerIdentity pi = this.provider.load(player.getUniqueId());
				if (pi != null) player.addIdentity(pi);
				
			} catch (Throwable t) {
				NeocoreAPI.getLogger().log(Level.SEVERE, "Problem when providing " + this.getProvider().getIdentityClass().getName() + "!", t);
			} finally {
				if (callback != null) callback.run();
			}
			
		});
		
		return ProvisionResult.QUEUED_DEFERRED_INJECTION;
		
	}
	
	@Override
	public void flush(NeoPlayer player, Runnable callback) {
		
		if (!(provider instanceof IdentityLinkage)) {
			
			return;
			
		}
		
		IdentityLinkage<?> link = this.getProviderAsLinkage();
		
		this.addWrappedRunnableToQueue(() -> {
			
			try {
				if (player.hasIdentity(link.getIdentityClass())) link.flush(player.getUniqueId());
			} catch (Throwable t) {
				NeocoreAPI.getLogger().log(Level.SEVERE, "Problem when flushing " + this.getProvider().getIdentityClass().getName() + "!", t);
			} finally {
				if (callback != null) callback.run();
			}
			
		});
		
	}
	
	@Override
	public void unload(NeoPlayer player, Runnable callback) {
		
		this.addWrappedRunnableToQueue(() -> {
			
			try {
				this.provider.unload(player.getUniqueId());
			} catch (Throwable t) {
				NeocoreAPI.getLogger().log(Level.SEVERE, "Problem when unloading " + this.getProvider().getIdentityClass().getName() + "!", t);
			} finally {
				if (callback != null) callback.run();
			}
			
		});
		
	}
	
	private void addWrappedRunnableToQueue(Runnable r) {
		this.queue.enqueue(new RunnableTask(this.delegator, r));
	}
	
}
