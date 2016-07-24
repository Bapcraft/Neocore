package io.neocore.database;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.typesafe.config.Config;

import io.neocore.api.database.DatabaseController;
import io.neocore.api.database.DatabaseManager;

public class DatabaseManagerImpl implements DatabaseManager {
	
	private Map<String, Class<? extends DatabaseController>> dbTypes;
	
	public DatabaseManagerImpl() {
		
		this.dbTypes = new HashMap<>();
		
	}
	
	@Override
	public void registerController(String name, Class<? extends DatabaseController> controller) {
		
		// Verify that we have a place for it.
		if (this.dbTypes.containsKey(name)) {
			
			String clazzName = this.dbTypes.get(name).getName();
			String repName = controller.getName();
			
			throw new IllegalStateException("A database controller named " + name + " of type " + clazzName + " already exists when registering " + repName);
			
		}
		
		NullPointerException noConstNpe = prepNoConstNpe(controller);
		
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
	public DatabaseController makeNewController(String name, Config cfg) {
		
		Class<? extends DatabaseController> clazz = this.dbTypes.get(name);
		NullPointerException noConstNpe = prepNoConstNpe(clazz);
		
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
	
	private static NullPointerException prepNoConstNpe(Class<? extends DatabaseController> clazz) {
		return new NullPointerException("No constructor for class " + clazz.getName() + " that takes a " + Config.class.getName() + " as the single argument.");
	}

	@Override
	public Collection<Class<? extends DatabaseController>> getControllers() {
		return this.dbTypes.values();
	}
	
}
