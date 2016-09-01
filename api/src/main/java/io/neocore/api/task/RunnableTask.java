package io.neocore.api.task;

/**
 * A task whose behavior is defined using a Runnable, here for compatibility
 * with other platforms.
 * 
 * @author treyzania
 */
public class RunnableTask extends Task {
	
	private final Runnable runnable;
	
	public RunnableTask(TaskDelegator tg, Runnable r) {
		
		super(tg);
		
		this.runnable = r;
		
	}
	
	@Override
	public void run() {
		this.runnable.run();
	}
	
}
