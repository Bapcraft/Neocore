package io.neocore.manage.server;

import java.util.List;
import java.util.logging.Logger;

import com.treyzania.jzania.ExoContainer;

public class Scheduler {
	
	private ExoContainer container;
	private List<Thread> activeThreads;
	
	public Scheduler(Logger log) {
		
		this.container = new ExoContainer(log);
		
	}
	
	public void invokeAsync(String name, Runnable proc) {
		
		Thread thread = new Thread(() -> {
			
			// Invoke the callback.
			this.container.invoke(name, proc);
			
			// Remove the thread from the list.
			this.activeThreads.removeIf(t -> t.getName().equals(name));
			
		}, name);
		
		this.activeThreads.add(thread);
		thread.start();
		
	}
	
}
