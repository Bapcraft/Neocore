package io.neocore.bukkit.events;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import io.neocore.api.module.Module;

public class NeocoreRevalidator implements Listener {
	
	private PluginManager manager;
	
	public NeocoreRevalidator(PluginManager manager) {
		
		this.manager = manager;
		
	}
	
	@EventHandler
	public void onPluginEnable(PluginEnableEvent event) {
		
		// Checks on the enabled plugin.
		Plugin enabled = event.getPlugin();
		if (enabled instanceof Module) {
			
			Module mod = (Module) enabled;
			Bukkit.getLogger().info("Enabled module " + mod.getName() + " v" + mod.getVersion() + " of type " + mod.getModuleType().name() + ".");
			
		}
		
		// Then general checks
		Plugin[] plugins = this.manager.getPlugins();
		int countEnabled = 0;
		for (Plugin p : plugins) {
			if (p.isEnabled()) countEnabled++;
		}
		
		Bukkit.getLogger().info("Currently loaded " + countEnabled + " plugins.");
		
	}
	
	@EventHandler
	public void onPluginDisable(PluginDisableEvent event) {
		
		@SuppressWarnings("unused")
		Plugin disabled = event.getPlugin();
		
		// TODO
		
	}
	
}
