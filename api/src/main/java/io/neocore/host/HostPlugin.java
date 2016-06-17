package io.neocore.host;

import java.util.List;

/**
 * Represents the plugin class used by whichever server is hosting a Neocore instance.
 * 
 * @author treyzania
 */
public interface HostPlugin {
	
	public List<HostService> getServices();
	
}
