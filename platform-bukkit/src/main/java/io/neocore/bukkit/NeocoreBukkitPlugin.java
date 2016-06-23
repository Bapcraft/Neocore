package io.neocore.bukkit;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.plugin.java.JavaPlugin;

import io.neocore.host.HostPlugin;
import io.neocore.host.HostServiceProvider;

public class NeocoreBukkitPlugin extends JavaPlugin implements HostPlugin {
	
	@Override
	public void onEnable() {
		
	}
	
	@Override
	public void onDisable() {
		
	}
	
	@Override
	public List<HostServiceProvider> getServices() {
		return new ArrayList<>();
	}
	
}
