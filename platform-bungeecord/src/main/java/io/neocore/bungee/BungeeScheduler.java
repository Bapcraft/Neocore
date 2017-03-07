package io.neocore.bungee;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.neocore.api.NeocoreAPI;
import io.neocore.api.host.Scheduler;
import io.neocore.api.host.ThreadInfo;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import net.md_5.bungee.api.scheduler.TaskScheduler;

public class BungeeScheduler implements Scheduler {

	private Plugin plugin;
	private TaskScheduler sched;

	private WeakHashMap<Thread, AsyncThreadInvocation> tree = new WeakHashMap<>();

	public BungeeScheduler(Plugin plug, TaskScheduler sched) {

		this.plugin = plug;
		this.sched = sched;

	}

	@Override
	public ThreadInfo invokeAsyncDelayed(Runnable run, long delayMillis) {

		Thread caller = Thread.currentThread();

		// Yucky hack to get things the way we want them to.
		StThreadInfo sti = new StThreadInfo();
		ScheduledTask st = this.sched.schedule(this.plugin, () -> {

			try {
				run.run();
			} catch (Throwable t) {

				tree.put(Thread.currentThread(), new AsyncThreadInvocation(caller));
				Logger log = NeocoreAPI.getLogger();

				log.log(Level.SEVERE, "Exception in thread \"" + Thread.currentThread().getName() + "\"", t);

				AsyncThreadInvocation ati = this.tree.get(Thread.currentThread());
				while (ati != null) {

					log.warning("Spawned by: " + ati.caller.getName());
					for (StackTraceElement ele : ati.stackTrace) {

						String message = String.format("%s.%s (%s:%s)", ele.getClassName(), ele.getMethodName(),
								ele.getFileName(), ele.getLineNumber());

						log.warning("    " + message);

					}

					ati = this.tree.get(ati.caller);

				}

			} finally { // This might be the first time I've ever used finally.
				sti.markEnded();
			}

		}, delayMillis, TimeUnit.MILLISECONDS);

		sti.init(st);

		return sti;

	}

	private static class AsyncThreadInvocation {

		public final Thread caller;
		public final List<StackTraceElement> stackTrace;

		public AsyncThreadInvocation(Thread t) {

			this.caller = t;
			this.stackTrace = new ArrayList<>(Arrays.asList(this.caller.getStackTrace()));

		}

	}

}
