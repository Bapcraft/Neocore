package io.neocore.bungee;

import java.util.concurrent.TimeUnit;

import io.neocore.api.host.Scheduler;
import io.neocore.api.host.ThreadInfo;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import net.md_5.bungee.api.scheduler.TaskScheduler;

public class BungeeScheduler implements Scheduler {
	
	private Plugin plugin;
	private TaskScheduler sched;
	
	public BungeeScheduler(Plugin plug, TaskScheduler sched) {
		
		this.plugin = plug;
		this.sched = sched;
		
	}

	@Override
	public ThreadInfo invokeAsyncDelayed(Runnable run, long delayMillis) {
		
		// Yucky hack to get things the way we want them to.
		StThreadInfo sti = new StThreadInfo();
		ScheduledTask st = this.sched.schedule(this.plugin, () -> {
			
			try {
				run.run();
			} catch (Throwable t) {
				
				/*
				 * Rethrow it because we don't actually care what happens to
				 * it, we just want to invoke the thing that marks the info as
				 * ended.
				 */
				throw t;
				
			} finally { // This might be the first time I've ever used finally.
				sti.markEnded();
			}
			
		}, delayMillis, TimeUnit.MILLISECONDS);
		
		sti.init(st);
		
		return sti;
		
	}
	
}
