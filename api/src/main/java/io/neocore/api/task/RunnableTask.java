package io.neocore.api.task;

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
