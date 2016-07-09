package io.neocore.module;

import java.util.Set;

public interface ModuleManager {
	
	public void registerModule(Module mod);
	public boolean isAcceptingRegistrations();
	
	public Set<Module> getModules();
	
}
