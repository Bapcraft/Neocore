package io.neocore.common.database;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.typesafe.config.Config;

import io.neocore.api.NeocoreAPI;
import io.neocore.api.ServiceManager;
import io.neocore.api.database.DatabaseConfig;
import io.neocore.api.database.DatabaseController;
import io.neocore.api.database.DatabaseManager;
import io.neocore.api.database.DatabaseService;
import io.neocore.api.database.DatabaseServiceProvider;

public class DatabaseManagerImpl implements DatabaseManager {
	
	private ServiceManager serviceManager;
	
	private Map<String, Class<? extends DatabaseController>> dbTypes;
	private Map<DatabaseService, DatabaseController> databases;
	
	private boolean configured = false;
	
	public DatabaseManagerImpl(ServiceManager man) {
		
		this.serviceManager = man;
		
		this.dbTypes = new HashMap<>();
		this.databases = new HashMap<>();
		
	}
	
	private DatabaseController makeNewController(Class<? extends DatabaseController> clazz, Config cfg) {
		
		NullPointerException noConstNpe = new NullPointerException(
			"No constructor for class " +
			clazz.getName() +
			" that takes a " +
			Config.class.getName() +
			" as the single argument."
		);
		
		try {
			
			Constructor<? extends DatabaseController> ctrlConst = clazz.getConstructor(Config.class);
			
			// Further verification
			if (ctrlConst != null) {
				
				try {
					return ctrlConst.newInstance(cfg);
				} catch (Exception e) {
					throw new RuntimeException("Error instantiating database controller!", e);
				}
				
			} else {
				throw noConstNpe;
			}
			
		} catch (NoSuchMethodException e) {
			throw noConstNpe;
		} catch (SecurityException e) {
			throw new RuntimeException("Error registering controller!", e);
		}
		
	}
	
	public void configure(DatabaseConfig config) {
		
		if (this.configured) throw new IllegalStateException("Database manager already configured.");
		this.configured = true;
		
		// Initialize the mappings.
		Map<Class<? extends DatabaseController>, List<DatabaseService>> controllers = new HashMap<>();
		Map<Class<? extends DatabaseController>, Config> ctrlConfigs = new HashMap<>();
		for (Class<? extends DatabaseController> ctrl : this.dbTypes.values()) {
			controllers.put(ctrl, new ArrayList<>());
		}
		
		// Tabulate the expected services.
		for (DatabaseService serv : DatabaseService.values()) {
			
			Class<? extends DatabaseController> ctrl = this.dbTypes.get(config.getControllerName(serv));
			controllers.get(ctrl).add(serv);
			ctrlConfigs.put(ctrl, config.getControllerConfig(serv)); // Not the cleanest.
			
		}
		
		for (Map.Entry<Class<? extends DatabaseController>, List<DatabaseService>> entry : controllers.entrySet()) {
			
			Class<? extends DatabaseController> clazz = entry.getKey();
			List<DatabaseService> services = entry.getValue();
			
			if (services.size() > 0) {
				
				// Create and initialize the database.
				DatabaseController dbc = this.makeNewController(clazz, ctrlConfigs.get(clazz));
				dbc.initialize();
				DatabaseServiceProvider[] provs = dbc.provide(services.toArray(new DatabaseService[services.size()]));
				
				for (int i = 0; i < provs.length; i++) {
					
					this.databases.put(services.get(i), dbc);
					this.serviceManager.registerServiceProvider(NeocoreAPI.getAgent().getHost(), services.get(i), provs[i]);
					
				}
				
			}
			
		}
		
	}
	
	@Override
	public void registerController(String name, Class<? extends DatabaseController> controller) {
		
		// Verify that we have a place for it.
		if (this.dbTypes.containsKey(name)) {
			
			String clazzName = this.dbTypes.get(name).getName();
			String repName = controller.getName();
			
			throw new IllegalStateException("A database controller named " + name + " of type " + clazzName + " already exists when registering " + repName);
			
		}
		
		NullPointerException noConstNpe = new NullPointerException(
			"No constructor for class " +
			controller.getName() +
			" that takes a " +
			Config.class.getName() +
			" as the single argument."
		);
		
		try {
			
			Constructor<? extends DatabaseController> ctrlConst = controller.getConstructor(Config.class);
			
			// Further verification
			if (ctrlConst != null) {
				this.dbTypes.put(name, controller);
			} else {
				throw noConstNpe;
			}
			
		} catch (NoSuchMethodException e) {
			throw noConstNpe;
		} catch (SecurityException e) {
			throw new RuntimeException("Error registering controller!", e);
		}
		
	}
	
	@Override
	public DatabaseController getControllerForName(String name) {
		return this.databases.get(name);
	}
	
	@Override
	public Collection<Class<? extends DatabaseController>> getControllerClasses() {
		return this.dbTypes.values();
	}

	@Override
	public Collection<DatabaseController> getControllers() {
		return this.databases.values();
	}
	
}
