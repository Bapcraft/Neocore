package io.neocore.module;

import java.util.Set;

import com.treyzania.jzania.data.MemberReferenceSet;

import io.neocore.module.Module;
import io.neocore.module.ModuleManager;

public class ModuleManagerImpl implements ModuleManager {
	
	private Set<Module> modules;
	private boolean open = true;
	
	public ModuleManagerImpl() {
		this.modules = new MemberReferenceSet<Module>(m -> m.getName().hashCode());
	}
	
	@Override
	public void registerModule(Module mod) {
		this.modules.add(mod);
	}
	
	@Override
	public boolean isAcceptingRegistrations() {
		return this.open;
	}
	
	@Override
	public Set<Module> getModules() {
		return this.modules;
	}
	
}
