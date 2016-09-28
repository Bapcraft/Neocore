package io.neocore.bukkit.shced;

import org.bukkit.scheduler.BukkitScheduler;

import io.neocore.api.host.ThreadInfo;

public class TidThreadInfo implements ThreadInfo {
	
	private BukkitScheduler sched;
	private int taskId;
	
	public TidThreadInfo(BukkitScheduler sched, int taskId) {
		
		this.sched = sched;
		this.taskId = taskId;
		
	}
	
	@Override
	public boolean isRunning() {
		return this.sched.isCurrentlyRunning(this.taskId);
	}

	@Override
	public void kill() {
		this.sched.cancelTask(this.taskId);
	}

}
