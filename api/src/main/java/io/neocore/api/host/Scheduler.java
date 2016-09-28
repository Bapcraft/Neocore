package io.neocore.api.host;

public interface Scheduler {
	
	/**
	 * Invokes the runnable in a separate thread in a specified number of milliseconds.
	 * 
	 * @param run The runnable.
	 * @param delayMillis The number of milliseconds to delay.
	 * @return The thread's info.
	 */
	public ThreadInfo invokeAsyncDelayed(Runnable run, long delayMillis);
	
	/**
	 * Begins invocation of the runnable in a separate thread.
	 * 
	 * @param runnable The runnable.
	 * @return The thread's info.
	 */
	public default ThreadInfo invokeAsync(Runnable runnable) {
		return this.invokeAsyncDelayed(runnable, 0);
	}
	
}
