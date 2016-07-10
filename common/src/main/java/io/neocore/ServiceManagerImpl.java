package io.neocore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import io.neocore.database.DatabaseService;
import io.neocore.host.HostService;
import io.neocore.module.Module;

public class ServiceManagerImpl implements ServiceManager {
	
	private List<RegisteredServiceImpl> services;
	
	public ServiceManagerImpl() {
		
		this.services = new ArrayList<>();
		
	}
	
	@Override
	public List<ServiceType> getUnprovidedServices() {
		
		// Assemble a list of all service types.
		List<ServiceType> types = new ArrayList<>();
		types.addAll(Arrays.asList(HostService.values()));
		types.addAll(Arrays.asList(DatabaseService.values()));
		
		// Loop through the services and see which there are to deal with.  This funny loop logic is to avoid checking types we've already ruled out.
		for (RegisteredServiceImpl sr : this.services) {
			
			Iterator<ServiceType> typesRemaining = types.iterator();
			
			while (typesRemaining.hasNext()) {
				
				// Check if compatible and remove if necessary.
				if (typesRemaining.next().getClassType().isAssignableFrom(sr.getServiceProvider().getClass())) typesRemaining.remove();
				
			}
			
		}
		
		return types;
		
	}

	@Override
	public void registerServiceProvider(Module mod, ServiceType type, ServiceProvider provider) {
		
		Class<? extends ServiceProvider> typeClazz = type.getClassType();
		Class<? extends ServiceProvider> clazz = provider.getClass();
		if (!typeClazz.isAssignableFrom(clazz)) throw new ClassCastException("The class " + clazz + " is not an instance of " + typeClazz + "!");
		
		if (this.getService(type) != null) throw new IllegalStateException("A provider already exists for type " + type.getName() + " when registering " + provider + "!");
		this.services.add(new RegisteredServiceImpl(mod, type, provider));
		
	}
	
	@Override
	public List<RegisteredService> getServices() {
		return new ArrayList<>(this.services);
	}
	
	@Override
	public RegisteredService getService(ServiceType type) {
		
		for (RegisteredServiceImpl rs : this.services) {
			if (rs.getType() == type) return rs;
		}
		
		return null;
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends ServiceProvider> T getService(Class<T> servClazz) {
		
		for (RegisteredServiceImpl rs : this.services) {
			if (servClazz.isAssignableFrom(rs.getServiceProvider().getClass())) return (T) rs.getServiceProvider();
		}
		
		return null;
		
	}
	
}
