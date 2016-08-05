package io.neocore.api.task;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Worker implements Runnable {
	
	private TaskQueue queue;
	private Logger reporter;
	
	private Thread thread;
	
	public Worker(TaskQueue q, Logger log) {
		
		this.queue = q;
		this.reporter = log;
		
		this.thread = new Thread(this, "TaskWorker-" + Integer.toHexString(this.hashCode())); // Need better names.
		
	}
	
	public void begin() {
		this.thread.start();
	}
	
	@Override
	public void run() {
		
		while (true) {
			
			Task task = this.queue.dequeue();
			
			try {
				task.run();
			} catch (Throwable t) {			
				this.reporter.log(Level.WARNING, "Error processing Task for delegator + " + task.getDelegator().getName() + "!", t);
			}
			
		}
		
	}
	
}
