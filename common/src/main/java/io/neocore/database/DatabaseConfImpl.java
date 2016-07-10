package io.neocore.database;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigObject;
import com.typesafe.config.ConfigValue;
import com.typesafe.config.ConfigValueType;

import io.neocore.ServiceType;

public class DatabaseConfImpl implements DatabaseConfiguraton {
	
	private Map<String, DatabaseController> databases;
	
	private Map<String, String> provisions;
	private String defaultProvider;
	
	public DatabaseConfImpl(DatabaseManager dbm, Config conf) {
		
		this.databases = new HashMap<>();
		this.provisions = new HashMap<>();
		
		ConfigObject ctrls = conf.getObject("controllers");
		for (String name : ctrls.keySet()) {
			
			ConfigValue ctrl = ctrls.get(name);
			Config cfg = null;
			
			// Determine how we should pull the configuration for the DB controller.
			if (ctrl.valueType() == ConfigValueType.OBJECT) {
				cfg = ((ConfigObject) ctrl).toConfig();
			} else if (ctrl.valueType() == ConfigValueType.STRING) {
				cfg = ConfigFactory.parseFile(new File((String) ctrl.unwrapped()));
			} else {
				throw new NullPointerException("Invalid controller definition for " + name + "! (" + ctrl.origin().lineNumber() + ")");
			}
			
			// Instantiate and map it.
			DatabaseController dbc = dbm.makeNewController(name, cfg);
			this.databases.put(name, dbc);
			
		}
		
		ConfigObject provs = conf.getObject("provisions");
		for (Entry<String, ConfigValue> e : provs.entrySet()) {
			
			String service = e.getKey();
			ConfigValue database = e.getValue();
			
			if (database.valueType() != ConfigValueType.STRING) throw new NullPointerException("Provider definition for " + service + " is not a string!");
			
			String dbName = database.unwrapped().toString();
			if (!this.databases.containsKey(service)) throw new NullPointerException("Provider name for " + service + " (\"" + dbName + "\") not found!");
			
			// If it's set to default then just set that one directly.
			if (!dbName.equals("default")) {
				this.defaultProvider = dbName;
			} else {
				this.provisions.put(service, dbName);
			}
			
		}
		
	}
	
	public DatabaseConfImpl(DatabaseManager dbm, File f) {
		this(dbm, ConfigFactory.parseFile(f));
	}
	
	@Override
	public boolean hasDefinition(ServiceType type) {
		return this.provisions.containsKey(type.getName());
	}
	
	@Override
	public DatabaseController getControllerForService(ServiceType type) {
		
		String typeStr = type.getName();
		
		// Determine which provider to use
		String ctrlName = this.provisions.getOrDefault(typeStr, this.defaultProvider);
		
		// Then just get it, or throw an exception if not provided.
		if (this.databases.containsKey(ctrlName)) {
			return this.databases.get(ctrlName);
		} else {
			throw new NullPointerException("Service " + typeStr + " does not have a provider defined and there is no default set!");
		}
		
	}
	
	@Override
	public Collection<DatabaseController> getControllers() {
		return this.databases.values();
	}
	
	@Override
	public DatabaseController getControllerByBrand(String brand) {
		
		for (DatabaseController c : this.databases.values()) {
			if (c.getBrand().equals(brand)) return c;
		}
		
		return null;
		
	}
	
}
