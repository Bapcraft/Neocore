package io.neocore.common.database;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import com.typesafe.config.Config;

import io.neocore.api.database.DatabaseConfig;
import io.neocore.api.database.DatabaseController;
import io.neocore.api.database.DatabaseManager;
import io.neocore.api.database.DatabaseService;

public class DatabaseManagerImpl implements DatabaseManager {
	
	private Map<String, Class<? extends DatabaseController>> dbTypes;
	private Map<DatabaseService, DatabaseController> databases;
	
	private boolean configured = false;
	
	public DatabaseManagerImpl() {
		
		this.dbTypes = new HashMap<>();
		this.databases = new HashMap<>();
		
	}
	
	private DatabaseController makeNewController(String name, Config cfg) {
		
		Class<? extends DatabaseController> clazz = this.dbTypes.get(name);
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
		
		for (DatabaseService serv : DatabaseService.values()) {
			
			// Pull out definitions.
			String name = config.getControllerName(serv);
			Config conf = config.getControllerConfig(serv);
			
			// Actually instantiate
			this.databases.put(serv, this.makeNewController(name, conf));
			
		}
		
		// Initialize everything in one go so we don't have things half initialized if instantiation of something goes awry.
		for (DatabaseController ctrl : this.databases.values()) {
			ctrl.initialize();
		}
		
		this.configured = true;
		
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
