package io.neocore.api.task;

/**
 * Represents something that issues tasks and can attempt some kind of special
 * recovery handling when something goes wrong executing them.
 * 
 * @author treyzania
 */
public interface TaskDelegator {

	/**
	 * @return The name of this delegator.
	 */
	public String getName();

	/**
	 * Called when something is thrown when executing a task.
	 * 
	 * @param task
	 *            The task that had an issue.
	 * @param problem
	 *            The Throwable thrown during execution
	 * @return <code>true</code> if the recovery was successful,
	 *         <code>false</code> if it was not.
	 */
	public default boolean recoverFromProblem(Task task, Throwable problem) {
		return false;
	}

}
