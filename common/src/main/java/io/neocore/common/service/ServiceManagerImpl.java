package io.neocore.common.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import io.neocore.api.NeocoreAPI;
import io.neocore.api.RegisteredService;
import io.neocore.api.ServiceManager;
import io.neocore.api.ServiceProvider;
import io.neocore.api.ServiceType;
import io.neocore.api.database.DatabaseService;
import io.neocore.api.host.HostService;
import io.neocore.api.module.Module;

public class ServiceManagerImpl implements ServiceManager {
	
	private List<RegisteredServiceImpl> services;
	private Map<Class<? extends ServiceProvider>, List<ServiceRegistrationHandler>> registrationHandlers;
	
	private boolean locked = false;
	
	public ServiceManagerImpl() {
		
		this.services = new ArrayList<>();
		this.registrationHandlers = new HashMap<>();
		
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
				ServiceType type = typesRemaining.next();
				if (type.getServiceClass() != null && type.getServiceClass().isAssignableFrom(sr.getServiceProvider().getClass())) typesRemaining.remove();
				
			}
			
		}
		
		return types;
		
	}
	
	@Override
	public void registerServiceProvider(Module mod, ServiceType type, ServiceProvider service) {
		
		if (this.locked) throw new IllegalStateException("Cannot add service, already initialized!");
		
		if (service == null) {
			
			NeocoreAPI.getLogger().warning("Tried to load a null service from module " + mod + " for type " + type.getName());
			return;
			
		}
		
		Class<? extends ServiceProvider> typeClazz = type.getServiceClass();
		Class<? extends ServiceProvider> clazz = service.getClass();
		if (!typeClazz.isAssignableFrom(clazz)) throw new ClassCastException("The class " + clazz + " is not an instance of " + typeClazz + "!");
		
		if (this.getService(type) != null) throw new IllegalStateException("A provider already exists for type " + type.getName() + " when registering " + service + "!");
		
		RegisteredServiceImpl rs = new RegisteredServiceImpl(mod, type, service);
		this.services.add(rs);
		
		// Then we go notify people
		for (Entry<Class<? extends ServiceProvider>, List<ServiceRegistrationHandler>> e : this.registrationHandlers.entrySet()) {
			if (e.getKey().isAssignableFrom(service.getClass())) {
				for (ServiceRegistrationHandler handler : e.getValue()) handler.onRegister(rs);
			}
		}
		
	}
	
	@Override
	public List<RegisteredService> getServices() {
		return new ArrayList<>(this.services);
	}
	
	@Override
	public RegisteredService getService(ServiceType type) {
		
		for (RegisteredServiceImpl rs : this.services) {
			if (rs.getType().equals(type)) return rs;
		}
		
		NeocoreAPI.getLogger().warning("Returning null for service query " + type.getName() + "!");
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
	
	public void registerRegistrationHandler(Class<? extends ServiceProvider> servClazz, ServiceRegistrationHandler handler) {
		
		// Get a valid list to add these things to.
		List<ServiceRegistrationHandler> handlers = null;
		if (!this.registrationHandlers.containsKey(servClazz)) {
			
			handlers = new ArrayList<>();
			this.registrationHandlers.put(servClazz, handlers);
			
		} else {
			handlers = this.registrationHandlers.get(servClazz);
		}
		
		// Then just actually add it.
		handlers.add(handler);
		
	}
	
	public synchronized void initializeServices() {
		
		if (this.locked) throw new IllegalStateException("Tried to initialize services that are already initialized!");
		this.services.forEach(s -> s.getServiceProvider().init());
		this.locked = true;
		
	}
	
}
