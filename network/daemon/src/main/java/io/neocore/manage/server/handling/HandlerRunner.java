package io.neocore.manage.server.handling;

public abstract class HandlerRunner implements Runnable {
	
	private volatile boolean running;
	
	public void disable() {
		this.running = false;
	}
	
	public boolean isActive() {
		return this.running;
	}
	
	@Override
	public void run() {
		
		this.running = true;
		
		while (this.running) {
			
			this.cycle();
			
		}
		
	}
	
	/**
	 * The actual behavior for the cycle.
	 */
	public abstract void cycle();
	
}
