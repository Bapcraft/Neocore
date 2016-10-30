package io.neocore.mysql;

import java.util.ArrayList;
import java.util.List;

import com.typesafe.config.Config;

import io.neocore.api.database.DatabaseController;
import io.neocore.api.database.DatabaseService;
import io.neocore.api.database.DatabaseServiceProvider;

public class MySqlController implements DatabaseController {
	
	private List<DatabaseServiceProvider> services;
	
	public MySqlController(Config config) {
		
		this.services = new ArrayList<>();
		
	}
	
	@Override
	public String getBrand() {
		return "MySQL-ORMLite";
	}
	
	@Override
	public void initialize() {
		
	}
	
	@Override
	public void shutdown() {
		
	}
	
	@Override
	public DatabaseServiceProvider[] provide(DatabaseService[] providing) {
		
		DatabaseServiceProvider[] provs = new DatabaseServiceProvider[providing.length];
		
		for (int i = 0; i < providing.length; i++) {
			
			for (DatabaseServiceProvider prov : this.services) {
				
				// Check if the types match, then stick it in the array.
				if (providing[i].isCompatible(prov)) {
					
					provs[i] = prov;
					break;
					
				}
				
			}
			
		}
		
		return provs;
		
	}
	
}
