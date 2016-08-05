package io.neocore.tasks;

import java.util.logging.Level;
import java.util.logging.Logger;

import io.neocore.api.task.Task;
import io.neocore.api.task.TaskDelegator;
import io.neocore.api.task.TaskQueue;

public class Worker implements Runnable {
	
	private TaskQueue queue;
	private Logger reporter;
	
	private volatile boolean isRunning;
	private Thread thread;
	
	public Worker(TaskQueue q, Logger log) {
		
		this.queue = q;
		this.reporter = log;
		
	}
	
	public void begin() {
		
		if (this.isRunning) throw new IllegalStateException("Worker is already working!");
		
		this.thread = new Thread(this, "TaskWorker-" + Integer.toHexString(this.hashCode())); // Need better names.
		this.thread.start();
		
	}
	
	@Override
	public void run() {
		
		if (this.isRunning) throw new IllegalStateException("Worker is already working!");
		this.isRunning = true;
		
		while (true) {
			
			Task task = this.queue.dequeue();
			TaskDelegator delegator = task.getDelegator();
			
			try {
				task.run();
			} catch (Throwable t) {			
				
				this.reporter.warning(
					String.format(
						"Problem when executing task %s, attemping recovery with %s... (Message: %s)",
						task.getName(),
						delegator.getName(),
						t.getMessage()
					)
				);
				
				// Attempt recovery and report accordingly.
				if (!delegator.recoverFromProblem(task, t)) {
					this.reporter.log(Level.WARNING, "Problem recovery for task " + task.getName() + " unsuccessful:", t);
				} else {
					this.reporter.info("Task recovery successful!");
				}
				
			}
			
		}
		
	}
	
}
