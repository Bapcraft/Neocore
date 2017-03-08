package io.neocore.common.tasks;

import java.util.logging.Logger;

import com.treyzania.jzania.ExoContainer;

import io.neocore.api.NeocoreAPI;
import io.neocore.api.task.Task;
import io.neocore.api.task.TaskDelegator;
import io.neocore.api.task.TaskQueue;

public class Worker implements Runnable {

	private ExoContainer container;

	private TaskQueue queue;
	private Logger reporter;

	private volatile boolean isRunning;
	private Thread thread;

	public Worker(TaskQueue q, Logger log) {

		this.queue = q;
		this.reporter = log;

		this.container = new ExoContainer(NeocoreAPI.getLogger());
		this.container.addHook(ct -> {

			if (ct.getThrown() instanceof InterruptedException)
				return;

			if (ct.getRan() instanceof Task) {

				Task t = (Task) ct.getRan();
				TaskDelegator d = t.getDelegator();

				// Attempt recovery and report accordingly.
				if (!d.recoverFromProblem(t, ct.getThrown())) {
					this.reporter.warning("Problem recovery for task " + t.getName() + " unsuccessful!");
				} else {
					this.reporter.info("Task recovery successful!");
				}

			} else {
				this.reporter.warning("Somehow picked up an execption callback for something that wasn't a task.");
			}

		});

	}

	public String getName() {
		return "TaskWorker-" + Integer.toHexString(this.hashCode());
	}

	public void begin() {

		if (this.isRunning)
			throw new IllegalStateException("Worker is already working!");

		this.thread = new Thread(this, this.getName() + "-Thread");
		this.thread.start();

	}

	public void stop() {
		this.isRunning = false;
	}
	
	@Override
	public void run() {

		if (this.isRunning)
			throw new IllegalStateException("Worker is already working!");

		synchronized (this) {

			this.isRunning = true;

			// Now actually do the looping.
			String exoName = String.format("CurrentTask(%s)", this.getName());
			while (this.isRunning) {

				// All the heavy lifting is done by the container.
				Task t = this.queue.dequeue();

				this.container.invoke(exoName, t);

			}

		}

		this.reporter.warning("Worker stopped!");

	}

}
