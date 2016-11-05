package io.neocore.jdbc;

import io.neocore.api.NeocoreAPI;
import io.neocore.api.module.JavaMicromodule;

public class JdbcMicromodule extends JavaMicromodule {
	
	@Override
	public void onEnable() {
		
		NeocoreAPI.getLogger().info("I surrender to JDBC.");
		
	}
	
	@Override
	public void onDisable() {
		
		NeocoreAPI.getLogger().info("Enough surrendering to JDBC for today.");
		
	}
	
}
