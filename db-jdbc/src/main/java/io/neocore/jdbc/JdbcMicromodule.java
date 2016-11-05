package io.neocore.jdbc;

import io.neocore.api.NeocoreAPI;
import io.neocore.api.database.DatabaseService;
import io.neocore.api.module.JavaMicromodule;
import io.neocore.api.player.PlayerManager;

public class JdbcMicromodule extends JavaMicromodule {
	
	@Override
	public void onEnable() {
		
		NeocoreAPI.getLogger().info("I surrender to JDBC.");
		
		// Tell the player manager that we can provide these services.
		PlayerManager pm = NeocoreAPI.getAgent().getPlayerManager();
		pm.addService(DatabaseService.PLAYER);
		pm.addService(DatabaseService.SESSION);
		
	}
	
	@Override
	public void onDisable() {
		
		NeocoreAPI.getLogger().info("Enough surrendering to JDBC for today.");
		
	}
	
}
