package io.neocore;

import java.util.Map;
import java.util.UUID;

import io.neocore.Neocore;
import io.neocore.database.DatabaseController;
import io.neocore.host.HostPlugin;
import io.neocore.player.NeoPlayer;
import io.neocore.player.PlayerManager;

public class NeocoreImpl implements Neocore {
	
	private final HostPlugin host;
	
	private PlayerManager playerManager;
	private Map<ServiceType, ServiceProvider> services;
	
	public NeocoreImpl(HostPlugin host) {
		this.host = host;
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
	public void registerServiceProvider(ServiceType type, ServiceProvider prov) {
		
		Class<? extends ServiceProvider> typeClazz = type.getClassType();
		Class<? extends ServiceProvider> clazz = prov.getClass();
		if (!typeClazz.isAssignableFrom(clazz)) throw new ClassCastException("The class " + clazz + " is not an instance of " + typeClazz + "!");
		
		if (this.services.containsKey(type)) throw new IllegalStateException("A provider already exists for type " + type.getName() + " when registering " + prov + "!");
		this.services.put(type, prov);
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends ServiceProvider> T getServiceProvider(ServiceType serviceType) {
		return (T) this.services.get(serviceType);
	}
	
}
