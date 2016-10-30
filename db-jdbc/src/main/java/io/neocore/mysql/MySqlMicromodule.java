package io.neocore.mysql;

import io.neocore.api.NeocoreAPI;
import io.neocore.api.module.JavaMicromodule;

public class MySqlMicromodule extends JavaMicromodule {
	
	@Override
	public void onEnable() {
		
		NeocoreAPI.getLogger().info("It's *My*SQL, not *Your*SQL!");
		
	}
	
	@Override
	public void onDisable() {
		
		NeocoreAPI.getLogger().info("Okay, it can be YourSQL, too...");
		
	}
	
}
