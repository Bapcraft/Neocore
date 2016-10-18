package io.neocore.api.task;

/**
 * Represents a low-priority job to be run asynchronously.
 * 
 * @author treyzania
 */
public abstract class Task implements Runnable {
	
	private final TaskDelegator delegator;
	
	public Task(TaskDelegator tg) {
		this.delegator = tg;
	}
	
	/**
	 * @return This task's delegator.
	 */
	public TaskDelegator getDelegator() {
		return this.delegator;
	}
	
	/**
	 * @return The name of this task, defaults to the class's simple name/
	 */
	public String getName() {
		return this.getClass().getSimpleName();
	}
	
}
