package io.neocore.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.neocore.api.host.Scheduler;
import io.neocore.api.host.ThreadInfo;

public class JavaNativeScheduler implements Scheduler {

	private Logger logger;

	private List<Thread> threads = Collections.synchronizedList(new ArrayList<>());
	int lastThreadId = 0;

	public JavaNativeScheduler(Logger log) {
		this.logger = log;
	}

	@Override
	public ThreadInfo invokeAsyncDelayed(Runnable run, long delayMillis) {

		Runnable wrapper = () -> this.wrapRunnable(run);

		Thread t = new Thread(wrapper, this.nextManagedThreadName());
		this.threads.add(t);

		this.logger.finest("NC-JNS spawning thread: " + t.getName());
		t.start();

		return new JnsThreadInfo(t);

	}

	private String nextManagedThreadName() {
		return String.format("ncJNS-Thread-%s", this.lastThreadId++);
	}

	private void wrapRunnable(Runnable exec) {

		Thread thread = Thread.currentThread();

		try {
			exec.run();
		} catch (Throwable t) {
			this.logger.log(Level.SEVERE, "[NC-JNS] Unhandled exception in thread \"" + thread.getName() + "\".", t);
		} finally {
			this.threads.remove(thread);
		}

	}

}
