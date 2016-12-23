package io.neocore.api.task;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

import io.neocore.api.NeocoreAPI;

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
		
		Task t = null;
		
		while (t == null) {
			
			try {
				t = this.queue.take();
			} catch (InterruptedException e) {
				NeocoreAPI.getLogger().warning("Interrupted while dequeueing.");
			}
			
		}
		
		return t;
		
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
