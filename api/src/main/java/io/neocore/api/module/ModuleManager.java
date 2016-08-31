package io.neocore.api.module;

import java.util.Set;

public interface ModuleManager {
	
	/**
	 * Adds a new module to the set of loaded modules, ready to be loaded.
	 * 
	 * @param mod The module.
	 */
	public void registerModule(Module mod);
	
	/**
	 * @return <code>true</code> if we can still accept module registrations, <code>false</code> otherwise.
	 */
	public boolean isAcceptingRegistrations();
	
	/**
	 * @return A set of currently loaded modules.
	 */
	public Set<Module> getModules();
	
	/**
	 * Enables all of the <b>micrmodules</b> in the set of loaded <b>modules</b>.
	 */
	public void enableMicromodules();
	
}
