package io.neocore.module;

import java.util.List;

public interface ModuleManager {
	
	public void registerModule(Module mod);
	public void isAcceptingRegistrations();
	
	public List<Module> getModules();
	
}
