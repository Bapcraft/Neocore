package io.neocore;

import java.util.Map;
import java.util.UUID;

import io.neocore.Neocore;
import io.neocore.database.DatabaseController;
import io.neocore.host.HostPlugin;
import io.neocore.module.Module;
import io.neocore.module.ModuleManager;
import io.neocore.module.ModuleManagerImpl;
import io.neocore.player.NeoPlayer;
import io.neocore.player.PlayerManager;

public class NeocoreImpl implements Neocore {
	
	private final HostPlugin host;
	
	private PlayerManager playerManager;
	private ModuleManagerImpl moduleManager;
	private ServiceManagerImpl serviceManager;
	
	private Map<ServiceType, ServiceProvider> services;
	
	public NeocoreImpl(HostPlugin host) {
		
		this.host = host;
		
		this.playerManager = new PlayerManager();
		this.moduleManager = new ModuleManagerImpl();
		
	}
	
	public HostPlugin getHost() {
		return this.host;
	}

	@Override
	public DatabaseController getDatabase() {
		return null; // TODO
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
	
}
