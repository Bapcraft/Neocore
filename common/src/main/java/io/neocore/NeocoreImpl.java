package io.neocore;

import java.util.Map;
import java.util.UUID;

import io.neocore.api.Neocore;
import io.neocore.api.NeocoreInstaller;
import io.neocore.api.ServiceManager;
import io.neocore.api.ServiceProvider;
import io.neocore.api.ServiceType;
import io.neocore.api.database.DatabaseManager;
import io.neocore.api.event.EventManager;
import io.neocore.api.host.HostPlugin;
import io.neocore.api.module.Module;
import io.neocore.api.module.ModuleManager;
import io.neocore.api.player.NeoPlayer;
import io.neocore.api.task.TaskQueue;
import io.neocore.database.DatabaseConfImpl;
import io.neocore.database.DatabaseManagerImpl;
import io.neocore.event.CommonEventManager;
import io.neocore.module.ModuleManagerImpl;
import io.neocore.player.CommonPlayerManager;
import io.neocore.tasks.DatabaseInitializerTask;
import io.neocore.tasks.NeocoreTaskDelegator;

public class NeocoreImpl implements Neocore {
	
	private final HostPlugin host;
	
	private CommonPlayerManager playerManager;
	
	private DatabaseManagerImpl dbManager;
	private ModuleManagerImpl moduleManager;
	private ServiceManagerImpl serviceManager;
	private CommonEventManager eventManager;
	
	private Map<ServiceType, ServiceProvider> services;
	
	private TaskQueue tasks;
	private NeocoreTaskDelegator taskDelegator;
	
	public NeocoreImpl(HostPlugin host) {
		
		this.host = host;
		
		NeocoreInstaller.install(this);
		
		// Define various managers.
		this.dbManager = new DatabaseManagerImpl();
		this.moduleManager = new ModuleManagerImpl(host.getMicromoduleDirectory());
		this.serviceManager = new ServiceManagerImpl();
		this.playerManager = new CommonPlayerManager(this.serviceManager, host.getPlayerInjector());
		this.eventManager = new CommonEventManager();
		
		// Set up the task queue.
		this.tasks = new TaskQueue();
		this.taskDelegator = new NeocoreTaskDelegator();
		this.tasks.enqueue(new DatabaseInitializerTask(this.taskDelegator, new DatabaseConfImpl(host.getDatabaseConfigFile()), this.dbManager));
		
		// Register the host right now.
		this.moduleManager.registerModule(host);
		
		// Enable things
		this.moduleManager.enableMicromodules();
		
	}
	
	@Override
	public HostPlugin getHost() {
		return this.host;
	}
	
	@Override
	public DatabaseManager getDatabaseManager() {
		return this.dbManager;
	}
	
	@Override
	public NeoPlayer getPlayer(UUID uuid) {
		return this.playerManager.getPlayer(uuid);
	}
	
	@Override
	public ServiceManager getServiceManager() {
		return this.serviceManager;
	}
	
	@Override
	public void registerServiceProvider(ServiceType type, ServiceProvider prov, Module module) {
		this.getServiceManager().registerServiceProvider(module, type, prov);
	}
	
	@Override
	public ServiceProvider getServiceProvider(ServiceType serviceType) {
		return this.services.get(serviceType);
	}
	
	@Override
	public ModuleManager getModuleManager() {
		return this.moduleManager;
	}
	
	@Override
	public EventManager getEventManager() {
		return this.eventManager;
	}
	
	@Override
	public TaskQueue getTaskQueue() {
		return this.tasks;
	}
	
}
