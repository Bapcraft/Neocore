package io.neocore.host;

import io.neocore.module.Module;
import io.neocore.module.ModuleType;

/**
 * Represents the plugin class used by whichever server is hosting a Neocore instance.
 * 
 * @author treyzania
 */
public interface HostPlugin extends Module {
	
	@Override
	default ModuleType getModuleType() {
		return ModuleType.HOST;
	}
	
}
