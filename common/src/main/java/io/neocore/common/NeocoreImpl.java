package io.neocore.common;

import java.util.Arrays;
import java.util.UUID;
import java.util.logging.Logger;

import io.neocore.api.Neocore;
import io.neocore.api.NeocoreAPI;
import io.neocore.api.ServiceManager;
import io.neocore.api.ServiceProvider;
import io.neocore.api.ServiceType;
import io.neocore.api.cmd.TreeCommand;
import io.neocore.api.database.DatabaseConfig;
import io.neocore.api.database.DatabaseManager;
import io.neocore.api.database.artifact.ArtifactService;
import io.neocore.api.database.artifact.ArtifactTypes;
import io.neocore.api.database.artifact.IdentifierManager;
import io.neocore.api.database.player.PlayerService;
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
import io.neocore.common.cmd.CommandAdminArtifact;
import io.neocore.common.cmd.CommandArtifactManager;
import io.neocore.common.cmd.CommandBroadcast;
import io.neocore.common.cmd.CommandCheckPerm;
import io.neocore.common.cmd.CommandCheckPerms;
import io.neocore.common.cmd.CommandCreateGroup;
import io.neocore.common.cmd.CommandException;
import io.neocore.common.cmd.CommandReloadPermissions;
import io.neocore.common.cmd.CommandSetGroupParent;
import io.neocore.common.cmd.CommandSetPermission;
import io.neocore.common.database.DatabaseConfImpl;
import io.neocore.common.database.DatabaseManagerImpl;
import io.neocore.common.event.CommonEventManager;
import io.neocore.common.module.ModuleManagerImpl;
import io.neocore.common.permissions.PermissionManagerImpl;
import io.neocore.common.player.CommonPlayerManager;
import io.neocore.common.player.LoginAcceptorImpl;
import io.neocore.common.player.PlayerManagerWrapperImpl;
import io.neocore.common.service.LoginServiceRegHandler;
import io.neocore.common.service.ServiceManagerImpl;
import io.neocore.common.tasks.Worker;

public class NeocoreImpl implements Neocore {
	
	private final UUID agentId;
	private final FullHostPlugin host;
	private volatile boolean active = false;
	
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
	
	public NeocoreImpl(FullHostPlugin host) {
		
		this.agentId = UUID.randomUUID();
		this.host = host;
		
		// Define various managers.
		this.serviceManager = new ServiceManagerImpl();
		this.eventManager = new CommonEventManager();
		this.extManager = new ExtensionManager();
		this.identManager = new IdentifierManager();
		this.dbManager = new DatabaseManagerImpl(this.serviceManager);
		this.moduleManager = new ModuleManagerImpl(this, host.getMicromoduleDirectory());
		this.playerManager = new CommonPlayerManager(this.serviceManager, this.eventManager, host.getScheduler());
		this.playerManWrapper = new PlayerManagerWrapperImpl(this.playerManager);
		this.permManager = new PermissionManagerImpl(this.playerManWrapper, this.serviceManager);
		
		// Set up acceptors
		this.loginAcceptor = new LoginAcceptorImpl(this.playerManager, this.eventManager, this.serviceManager, this.identManager, host.getContexts());
		this.serviceManager.registerRegistrationHandler(LoginService.class, new LoginServiceRegHandler(this.loginAcceptor));
		
		// Set up the task queue.
		this.tasks = new TaskQueue();
		
	}
	
	public synchronized void init() {
		
		// Sanity check.
		if (this.active) return;
		Logger log = NeocoreAPI.getLogger();
		
		// Register ALL these commands.
		log.info("Installing commands...");
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
				new CommandCheckPerms(),
				new CommandSetGroupParent())));
		this.host.registerCommand(new CommandCheckPerm());
		this.host.registerCommand(new CommandException());
		this.host.registerCommand(new CommandAdminArtifact(this.serviceManager, "blame", ArtifactTypes.ADMIN_EVIDENCE, "neocore.cmd.blame"));
		this.host.registerCommand(new CommandAdminArtifact(this.serviceManager, "warn", ArtifactTypes.ADMIN_WARNING, "neocore.cmd.warn"));
		
		// Register the host right now.
		this.moduleManager.registerModule(this.host);
		
		// Enable micromodules.
		log.info("Setting up micromodules...");
		this.moduleManager.enableMicromodules();
		
		// Set up databases.
		log.info("Initializing database(s)...");
		DatabaseConfig dbc = new DatabaseConfImpl(this.host.getDatabaseConfigFile());
		log.finer("Loaded Configs: " + dbc.getNumDiscreteConfigs());
		this.dbManager.configure(dbc);
		
		// Now that the database(s) is(/are) set up, we can init the services themselves.
		log.info("Initializing services...");
		this.serviceManager.initializeServices();
		
		log.info("Doing final cleanup and things...");
		
		// TODO Move these elsewhere.
		this.identManager.setArtifactService(this.serviceManager.getService(ArtifactService.class));
		this.identManager.setPlayerService(this.serviceManager.getService(PlayerService.class));
		
		// This is OK
		this.host.getScheduler().invokeAsync(new Worker(this.tasks, NeocoreAPI.getLogger()));
		
		// Announce.
		NeocoreAPI.announceCompletion();
		this.active = true;
		
	}
	
	public synchronized void shutdown() {
		
		// Sanity check.
		if (!this.active) return;
		
		Logger log = NeocoreAPI.getLogger();
		
		// Disable micromodules...
		log.info("Disabling micromodules...");
		this.moduleManager.disableMicromodules();
		
		this.active = false;
		
	}
	
	@Override
	public UUID getAgentId() {
		return this.agentId;
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
		return this.active;
	}
	
}
