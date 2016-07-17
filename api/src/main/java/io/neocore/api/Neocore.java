package io.neocore.api;

import java.util.UUID;

import io.neocore.api.database.DatabaseManager;
import io.neocore.api.host.HostPlugin;
import io.neocore.api.module.Module;
import io.neocore.api.module.ModuleManager;
import io.neocore.api.player.NeoPlayer;

/**
 * Represents the main agent for accessing general components of Neocore.
 * 
 * @author treyzania
 */
public interface Neocore {
	
	public HostPlugin getHost();
	public DatabaseManager getDatabaseManager();
	
	public NeoPlayer getPlayer(UUID uuid);
	
	public ModuleManager getModuleManager();
	
	public ServiceManager getServiceManager();
	public void registerServiceProvider(ServiceType type, ServiceProvider prov, Module module);
	
	public ServiceProvider getServiceProvider(ServiceType serviceType);
	
}
