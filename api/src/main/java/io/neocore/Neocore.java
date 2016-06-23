package io.neocore;

import java.util.UUID;

import io.neocore.database.DatabaseController;
import io.neocore.host.HostPlugin;
import io.neocore.player.NeoPlayer;

/**
 * Represents the main agent for accessing general components of Neocore.
 * 
 * @author treyzania
 */
public interface Neocore {
	
	public HostPlugin getHost();
	public DatabaseController getDatabase();
	
	public NeoPlayer getPlayer(UUID uuid);
	
	public void registerServiceProvider(ServiceType type, ServiceProvider prov);
	
	public <T extends ServiceProvider> T getServiceProvider(ServiceType serviceType);
	
}
