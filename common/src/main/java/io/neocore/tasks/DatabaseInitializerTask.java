package io.neocore.tasks;

import java.util.logging.Logger;

import io.neocore.api.NeocoreAPI;
import io.neocore.api.database.DatabaseConfig;
import io.neocore.api.task.Task;
import io.neocore.api.task.TaskDelegator;
import io.neocore.database.DatabaseManagerImpl;

public class DatabaseInitializerTask extends Task {
	
	private DatabaseConfig config;
	private DatabaseManagerImpl manager;
	
	public DatabaseInitializerTask(TaskDelegator tg, DatabaseConfig conf, DatabaseManagerImpl dbm) {
		
		super(tg);
		
		this.config = conf;
		this.manager = dbm;
		
	}
	
	@Override
	public void run() {
		
		Logger log = NeocoreAPI.getLogger();
		
		// All the actual behavior is done in the manager
		log.info("Initializing database(s)...");
		this.manager.configure(config);
		log.info("Done!");
		
	}
	
}
