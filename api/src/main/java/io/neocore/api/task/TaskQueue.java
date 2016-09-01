package io.neocore.api.task;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * A list of tasks to be run in a FIFO order.
 * 
 * @author treyzania
 */
public class TaskQueue {
	
	private final BlockingDeque<Task> queue;
	
	public TaskQueue() {
		
		// Linked list because add/remove is really fast.
		this.queue = new LinkedBlockingDeque<>();
		
	}
	
	/**
	 * Gets the next task added to the queue, blocking until one is available.
	 * 
	 * @return The next task.
	 */
	public Task dequeue() {
		
		try {
			return this.queue.take();
		} catch (InterruptedException e) {
			throw new IllegalStateException("Somehow interrupted while dequeueing!");
		}
		
	}
	
	/**
	 * Adds a new task to the queue.
	 * 
	 * @param task The task to add.
	 */
	public void enqueue(Task task) {
		this.queue.add(task);
	}
	
}
