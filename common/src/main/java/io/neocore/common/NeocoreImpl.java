package io.neocore.common;

import java.util.UUID;

import io.neocore.api.Neocore;
import io.neocore.api.NeocoreInstaller;
import io.neocore.api.ServiceManager;
import io.neocore.api.ServiceProvider;
import io.neocore.api.ServiceType;
import io.neocore.api.database.DatabaseManager;
import io.neocore.api.event.EventManager;
import io.neocore.api.host.HostPlugin;
import io.neocore.api.host.login.LoginAcceptor;
import io.neocore.api.host.login.LoginService;
import io.neocore.api.module.Module;
import io.neocore.api.module.ModuleManager;
import io.neocore.api.player.NeoPlayer;
import io.neocore.api.task.TaskQueue;
import io.neocore.common.database.DatabaseManagerImpl;
import io.neocore.common.event.CommonEventManager;
import io.neocore.common.module.ModuleManagerImpl;
import io.neocore.common.player.CommonPlayerManager;
import io.neocore.common.player.LoginAcceptorImpl;
import io.neocore.common.service.LoginServiceRegHandler;
import io.neocore.common.service.ServiceManagerImpl;
import io.neocore.common.tasks.DatabaseInitializerTask;
import io.neocore.common.tasks.NeocoreTaskDelegator;

public class NeocoreImpl implements Neocore {
	
	private final FullHostPlugin host;
	
	private CommonPlayerManager playerManager;
	
	private DatabaseManagerImpl dbManager;
	private ModuleManagerImpl moduleManager;
	private ServiceManagerImpl serviceManager;
	private CommonEventManager eventManager;
	
	private LoginAcceptor loginAcceptor;
	
	private TaskQueue tasks;
	private NeocoreTaskDelegator taskDelegator;
	
	public NeocoreImpl(FullHostPlugin host) {
		
		this.host = host;
		
		NeocoreInstaller.install(this);
		
		// Define various managers.
		this.serviceManager = new ServiceManagerImpl();
		this.dbManager = new DatabaseManagerImpl(this.serviceManager);
		this.moduleManager = new ModuleManagerImpl(host.getMicromoduleDirectory());
		this.playerManager = new CommonPlayerManager(this.serviceManager, host.getPlayerInjector());
		this.eventManager = new CommonEventManager();
		
		// Set up acceptors
		this.loginAcceptor = new LoginAcceptorImpl(this.playerManager, this.eventManager, this.serviceManager, host.getContexts());
		
		// Set up the service manager with the listeners we want
		this.serviceManager.registerRegistrationHandler(LoginService.class, new LoginServiceRegHandler(this.loginAcceptor));
		
		// Set up the task queue.
		this.tasks = new TaskQueue();
		this.taskDelegator = new NeocoreTaskDelegator();
		this.tasks.enqueue(new DatabaseInitializerTask(this.taskDelegator, host, this.dbManager));
		
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
	public ServiceProvider getService(ServiceType serviceType) {
		return this.getServiceManager().getService(serviceType.getServiceClass());
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
