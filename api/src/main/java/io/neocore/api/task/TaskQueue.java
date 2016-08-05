package io.neocore.api.task;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class TaskQueue {
	
	private final BlockingDeque<Task> queue;
	
	public TaskQueue() {
		
		// Linked list because add/remove is really fast.
		this.queue = new LinkedBlockingDeque<>();
		
	}
	
	public Task dequeue() {
		
		try {
			return this.queue.take();
		} catch (InterruptedException e) {
			throw new IllegalStateException("Somehow interrupted while dequeueing!");
		}
		
	}
	
	public void enqueue(Task task) {
		this.queue.add(task);
	}
	
}
