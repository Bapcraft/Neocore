package io.neocore.api.host;

import java.io.File;
import java.util.List;

import io.neocore.api.NeocoreConfig;
import io.neocore.api.module.Module;
import io.neocore.api.module.ModuleType;

/**
 * Represents the plugin class used by whichever server is hosting a Neocore instance.
 * 
 * @author treyzania
 */
public interface HostPlugin extends Module {
	
	public File getMicromoduleDirectory();
	
	public NeocoreConfig getNeocoreConfig();
	public File getDatabaseConfigFile();
	public HostPlayerInjector getPlayerInjector();
	
	@Override
	public default ModuleType getModuleType() {
		return ModuleType.HOST;
	}
	
	public HostContext getPrimaryContext();
	public List<Context> getContexts();
	
}
