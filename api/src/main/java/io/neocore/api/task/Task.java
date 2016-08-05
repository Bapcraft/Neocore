package io.neocore.api.task;

public abstract class Task {
	
	private final TaskDelegator delegator;
	
	public Task(TaskDelegator tg) {
		this.delegator = tg;
	}
	
	public TaskDelegator getDelegator() {
		return this.delegator;
	}
	
	public abstract void run();
	
}
