package io.neocore.bukkit.shced;

import java.util.concurrent.Future;

import io.neocore.api.host.ThreadInfo;

public class BukkitThreadInfo implements ThreadInfo {
	
	private Future<Void> future;
	
	public BukkitThreadInfo(Future<Void> fut) {
		this.future = fut;
	}
	
	@Override
	public boolean isRunning() {
		return !this.future.isDone();
	}

	@Override
	public void kill() {
		
		if (!this.future.isDone() && !this.future.isCancelled()) {
			this.future.cancel(true);
		}
		
	}

}
