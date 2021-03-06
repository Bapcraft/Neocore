package io.neocore.api;

import java.util.UUID;

import io.neocore.api.database.DatabaseManager;
import io.neocore.api.database.artifact.IdentifierManager;
import io.neocore.api.event.EventManager;
import io.neocore.api.host.HostPlugin;
import io.neocore.api.module.Module;
import io.neocore.api.module.ModuleManager;
import io.neocore.api.player.NeoPlayer;
import io.neocore.api.player.PlayerManager;
import io.neocore.api.player.extension.ExtensionManager;
import io.neocore.api.player.permission.PermissionManager;
import io.neocore.api.task.TaskQueue;

/**
 * Represents the main agent for accessing general components of Neocore.
 * 
 * @author treyzania
 */
public interface Neocore extends AgentIdentity {

	/**
	 * @return A reference to the host plugin for the server we're running on
	 *         top of.
	 */
	public HostPlugin getHost();

	/**
	 * @return If the server is part of a larger network.
	 */
	@Override
	public default boolean isNetworked() {
		return this.getHost().getNeocoreConfig().isNetworked();
	}

	/**
	 * @return The name of the server on the network.
	 */
	public default String getServerName() {
		return this.getHost().getNeocoreConfig().getServerName();
	}

	/**
	 * @return The name of the network the server is on, if on a network.
	 */
	public default String getNetworkName() {
		return this.isNetworked() ? this.getHost().getNeocoreConfig().getNetworkName() : null;
	}

	/**
	 * @return The player manager used by Neocore.
	 */
	public PlayerManager getPlayerManager();

	/**
	 * @param uuid
	 *            The player's UUID
	 * @return the NeoPlayer with the specified UUID
	 */
	public NeoPlayer getPlayer(UUID uuid);

	/**
	 * @return The database manager used by Neocore.
	 */
	public DatabaseManager getDatabaseManager();

	/**
	 * @return The module manager used by Neocore.
	 */
	public ModuleManager getModuleManager();

	/**
	 * @return The service manager used by Neocore.
	 */
	public ServiceManager getServiceManager();

	/**
	 * Registers a service provider for the specified service type, from
	 * whichever module specified, in the service manager.
	 * 
	 * @param type
	 *            The type of service the provider is providing
	 * @param prov
	 *            The provider itself
	 * @param module
	 *            The module that supplied the provider (usually set to "this")
	 */
	public void registerServiceProvider(ServiceType type, ServiceProvider prov, Module module);

	/**
	 * Gets the service provider specified from the service manager.
	 * 
	 * @see ServiceManager.getService
	 * 
	 * @param serviceType
	 *            The type of service we're looking for
	 * @return The service provider itself.
	 */
	public ServiceProvider getService(ServiceType serviceType);

	/**
	 * @return The event manager used by Neocore.
	 */
	public EventManager getEventManager();

	/**
	 * @return The extension manager used by Neocore.
	 */
	public ExtensionManager getExtensionManager();

	/**
	 * @return The permission manager used by Neocore.
	 */
	public PermissionManager getPermissionManager();

	/**
	 * @return The identifier manager used by Neocore.
	 */
	public IdentifierManager getIdentifierManager();

	/**
	 * @return The async task queue used by the Neocore async worker.
	 */
	public TaskQueue getTaskQueue();

	/**
	 * @return <code>true</code> if everything has been set up properly.
	 */
	public boolean isInited();

}
