package io.neocore.common.tasks;

import io.neocore.api.Neocore;
import io.neocore.api.NeocoreAPI;
import io.neocore.api.ServiceManager;
import io.neocore.api.database.artifact.ArtifactService;
import io.neocore.api.database.artifact.IdentifierManager;
import io.neocore.api.database.player.PlayerService;
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
		
		Neocore nc = NeocoreAPI.getAgent();
		IdentifierManager im = nc.getIdentifierManager();
		ServiceManager sm = nc.getServiceManager();
		im.setArtifactService(sm.getService(ArtifactService.class));
		im.setPlayerService(sm.getService(PlayerService.class));
		
		NeocoreAPI.getLogger().info("Initialized services!");
		
	}

}
