package io.neocore.common;

import java.util.Arrays;
import java.util.UUID;

import io.neocore.api.Neocore;
import io.neocore.api.NeocoreInstaller;
import io.neocore.api.ServiceManager;
import io.neocore.api.ServiceProvider;
import io.neocore.api.ServiceType;
import io.neocore.api.cmd.TreeCommand;
import io.neocore.api.database.DatabaseManager;
import io.neocore.api.database.artifact.IdentifierManager;
import io.neocore.api.event.EventManager;
import io.neocore.api.host.HostPlugin;
import io.neocore.api.host.login.LoginAcceptor;
import io.neocore.api.host.login.LoginService;
import io.neocore.api.module.Module;
import io.neocore.api.module.ModuleManager;
import io.neocore.api.player.NeoPlayer;
import io.neocore.api.player.PlayerManager;
import io.neocore.api.player.extension.ExtensionManager;
import io.neocore.api.player.permission.PermissionManager;
import io.neocore.api.task.TaskQueue;
import io.neocore.common.cmd.CommandActiveUserManager;
import io.neocore.common.cmd.CommandArtifactManager;
import io.neocore.common.cmd.CommandBroadcast;
import io.neocore.common.cmd.CommandCheckPerm;
import io.neocore.common.cmd.CommandCheckPerms;
import io.neocore.common.cmd.CommandCreateGroup;
import io.neocore.common.cmd.CommandException;
import io.neocore.common.cmd.CommandReloadPermissions;
import io.neocore.common.cmd.CommandSetPermission;
import io.neocore.common.database.DatabaseManagerImpl;
import io.neocore.common.event.CommonEventManager;
import io.neocore.common.module.ModuleManagerImpl;
import io.neocore.common.permissions.PermissionManagerImpl;
import io.neocore.common.player.CommonPlayerManager;
import io.neocore.common.player.LoginAcceptorImpl;
import io.neocore.common.player.PlayerManagerWrapperImpl;
import io.neocore.common.service.LoginServiceRegHandler;
import io.neocore.common.service.ServiceManagerImpl;
import io.neocore.common.tasks.DatabaseInitializerTask;
import io.neocore.common.tasks.NeocoreTaskDelegator;
import io.neocore.common.tasks.ServiceInitializationTask;

public class NeocoreImpl implements Neocore {
	
	private final FullHostPlugin host;
	
	private CommonPlayerManager playerManager;
	private PlayerManagerWrapperImpl playerManWrapper;
	
	private DatabaseManagerImpl dbManager;
	private ModuleManagerImpl moduleManager;
	private ServiceManagerImpl serviceManager;
	private CommonEventManager eventManager;
	private ExtensionManager extManager;
	private PermissionManagerImpl permManager;
	private IdentifierManager identManager;
	
	private LoginAcceptor loginAcceptor;
	
	private TaskQueue tasks;
	private NeocoreTaskDelegator taskDelegator;
	
	public NeocoreImpl(FullHostPlugin host) {
		
		this.host = host;
		
		NeocoreInstaller.install(this);
		
		// Define various managers.
		this.serviceManager = new ServiceManagerImpl();
		this.eventManager = new CommonEventManager();
		this.extManager = new ExtensionManager();
		this.identManager = new IdentifierManager();
		this.dbManager = new DatabaseManagerImpl(this.serviceManager);
		this.moduleManager = new ModuleManagerImpl(host.getMicromoduleDirectory());
		this.playerManager = new CommonPlayerManager(this.serviceManager, this.eventManager, host.getScheduler());
		this.playerManWrapper = new PlayerManagerWrapperImpl(this.playerManager);
		this.permManager = new PermissionManagerImpl(this.playerManWrapper, this.serviceManager);
		
		// Set up acceptors
		this.loginAcceptor = new LoginAcceptorImpl(this.playerManager, this.eventManager, this.serviceManager, this.identManager, host.getContexts());
		
		// Set up the service manager with the listeners we want
		this.serviceManager.registerRegistrationHandler(LoginService.class, new LoginServiceRegHandler(this.loginAcceptor));
		
		// Set up the task queue.
		this.tasks = new TaskQueue();
		this.taskDelegator = new NeocoreTaskDelegator();
		this.tasks.enqueue(new DatabaseInitializerTask(this.taskDelegator, host, this.dbManager));
		this.tasks.enqueue(new ServiceInitializationTask(this.taskDelegator, this.serviceManager));
		
		// Register commands.
		this.host.registerCommand(new CommandActiveUserManager(this.playerManWrapper));
		this.host.registerCommand(new CommandArtifactManager(this.serviceManager));
		this.host.registerCommand(new CommandBroadcast(this.serviceManager));
		this.host.registerCommand(new TreeCommand(
			"permissions",
			"neocore.cmd.permissions",
			Arrays.asList(
				new CommandCreateGroup(this.serviceManager),
				new CommandReloadPermissions(),
				new CommandSetPermission(),
				new CommandCheckPerms())));
		this.host.registerCommand(new CommandCheckPerm());
		this.host.registerCommand(new CommandException());
		
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
	public PlayerManager getPlayerManager() {
		return this.playerManWrapper;
	}
	
	public CommonPlayerManager getPlayerAssembler() {
		return this.playerManager;
	}
	
	@Override
	public NeoPlayer getPlayer(UUID uuid) {
		return this.getPlayerManager().getPlayer(uuid);
	}
	
	@Override
	public DatabaseManager getDatabaseManager() {
		return this.dbManager;
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
	public ExtensionManager getExtensionManager() {
		return this.extManager;
	}
	
	@Override
	public PermissionManager getPermissionManager() {
		return this.permManager;
	}
	
	@Override
	public IdentifierManager getIdentifierManager() {
		return this.identManager;
	}
	
	@Override
	public TaskQueue getTaskQueue() {
		return this.tasks;
	}

	@Override
	public boolean isInited() {
		return true; // FIXME
	}
	
}
