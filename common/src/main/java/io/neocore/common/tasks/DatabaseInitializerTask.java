package io.neocore.common.tasks;

import java.util.logging.Logger;

import io.neocore.api.NeocoreAPI;
import io.neocore.api.database.DatabaseConfig;
import io.neocore.api.host.HostPlugin;
import io.neocore.api.task.Task;
import io.neocore.api.task.TaskDelegator;
import io.neocore.common.database.DatabaseConfImpl;
import io.neocore.common.database.DatabaseManagerImpl;

public class DatabaseInitializerTask extends Task {
	
	private HostPlugin plugin;
	private DatabaseManagerImpl manager;
	
	public DatabaseInitializerTask(TaskDelegator tg, HostPlugin plugin, DatabaseManagerImpl dbm) {
		
		super(tg);
		
		this.plugin = plugin;
		this.manager = dbm;
		
	}
	
	@Override
	public void run() {
		
		Logger log = NeocoreAPI.getLogger();
		
		// All the actual behavior is done in the manager
		log.info("Initializing database(s)...");
		DatabaseConfig dbc = new DatabaseConfImpl(plugin.getDatabaseConfigFile());
		log.info("Loaded Configs: " + dbc.getNumDiscreteConfigs());
		this.manager.configure(dbc);
		log.info("Done!");
		
	}
	
}
