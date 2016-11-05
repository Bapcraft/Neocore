package io.neocore.common.tasks;

import io.neocore.api.NeocoreAPI;
import io.neocore.api.task.Task;
import io.neocore.api.task.TaskDelegator;
import io.neocore.common.service.ServiceManagerImpl;

public class ServiceInitializationTask extends Task {
	
	private ServiceManagerImpl manager;
	
	public ServiceInitializationTask(TaskDelegator tg, ServiceManagerImpl smi) {
		
		super(tg);
		
		this.manager = smi;
		
	}

	@Override
	public void run() {
		
		NeocoreAPI.getLogger().info("Initializing services...");
		this.manager.initializeServices();
		NeocoreAPI.getLogger().info("Initialized services!");
		
	}

}
