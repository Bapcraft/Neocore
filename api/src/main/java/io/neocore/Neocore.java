package io.neocore;

import java.util.UUID;

import io.neocore.database.DatabaseManager;
import io.neocore.host.HostPlugin;
import io.neocore.module.Module;
import io.neocore.module.ModuleManager;
import io.neocore.player.NeoPlayer;

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
