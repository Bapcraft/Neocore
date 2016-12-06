package io.neocore.bungee;

import io.neocore.api.host.ThreadInfo;
import net.md_5.bungee.api.scheduler.ScheduledTask;

public class StThreadInfo implements ThreadInfo {
	
	private ScheduledTask task;
	private boolean running;
	
	public StThreadInfo() {
		
	}
	
	protected void init(ScheduledTask st) {
		
		this.task = st;
		this.running = true;
		
	}
	
	protected void markEnded() {
		this.running = false;
	}
	
	@Override
	public boolean isRunning() {
		return this.running;
	}

	@Override
	public void kill() {
		this.task.cancel(); // Not totally ok.
	}

}
