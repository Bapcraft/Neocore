package io.neocore.bukkit.shced;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import io.neocore.api.host.Scheduler;
import io.neocore.api.host.ThreadInfo;

public class NeoBukkitScheduler implements Scheduler {
	
	private Plugin plugin;
	private BukkitScheduler sched;
	
	public NeoBukkitScheduler(Plugin plug, BukkitScheduler sched) {
		
		this.plugin = plug;
		this.sched = sched;
		
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public ThreadInfo invokeAsyncDelayed(Runnable run, long delayMillis) {
		
		int taskId = this.sched.scheduleAsyncDelayedTask(this.plugin, run, delayMillis);
		return new TidThreadInfo(this.sched, taskId);
		
	}

	@Override
	public ThreadInfo invokeAsync(Runnable runnable) {
		
		// Yuck.
		Future<Void> fut = this.sched.callSyncMethod(this.plugin, new Callable<Void>() {

			@Override
			public Void call() throws Exception {
				
				runnable.run();
				return null;
				
			}
			
		});
		
		return new BukkitThreadInfo(fut);
	}
	
}
