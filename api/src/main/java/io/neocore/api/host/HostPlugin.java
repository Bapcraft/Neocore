package io.neocore.api.host;

import io.neocore.api.module.Module;
import io.neocore.api.module.ModuleType;

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
