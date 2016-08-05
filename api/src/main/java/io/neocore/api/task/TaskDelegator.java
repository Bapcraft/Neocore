package io.neocore.api.task;

public interface TaskDelegator {
	
	public String getName();
	
	/**
	 * Called when something is thrown when executing a task.
	 * 
	 * @param task The task that had an issue.
	 * @param problem The Throwable thrown during execution
	 * @return <code>true</code> if the recovery was successful, <code>false</code> if it was not.
	 */
	public default boolean recoverFromProblem(Task task, Throwable problem) {
		return false;
	}
	
}
