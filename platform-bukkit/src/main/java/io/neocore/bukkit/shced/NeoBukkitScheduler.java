package io.neocore.bukkit.shced;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

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
		
		BukkitTask task = this.sched.runTaskAsynchronously(this.plugin, runnable);
		return new TidThreadInfo(this.sched, task.getTaskId());
		
	}
	
}
