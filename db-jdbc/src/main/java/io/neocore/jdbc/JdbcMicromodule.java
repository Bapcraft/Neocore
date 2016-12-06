package io.neocore.jdbc;

import io.neocore.api.Neocore;
import io.neocore.api.NeocoreAPI;
import io.neocore.api.module.JavaMicromodule;
import io.neocore.api.module.ModuleType;

public class JdbcMicromodule extends JavaMicromodule {
	
	@Override
	public void onEnable() {
		
		NeocoreAPI.getLogger().info("I surrender to JDBC.");
		Neocore neo = NeocoreAPI.getAgent();
		
		// Register the controller itself.
		neo.getDatabaseManager().registerController("JDBC", JdbcController.class);
		
	}
	
	@Override
	public void onDisable() {
		
		NeocoreAPI.getLogger().info("Enough surrendering to JDBC for today.");
		
	}

	@Override
	public ModuleType getModuleType() {
		return ModuleType.DATABASE;
	}
	
}
